package com.example.nivapptirgul.ui.fragments.ListFragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.di.DaggerAppComponent
import com.example.nivapptirgul.ui.ListViewModel
import com.example.nivapptirgul.ui.fragments.ScopedFragment
import com.example.nivapptirgul.ui.fragments.addReminder.NEW_REMINDER
import com.example.nivapptirgul.ui.fragments.recycler.SwipeAdapter
import com.example.nivapptirgul.ui.service.NetworkService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragment : ScopedFragment() {

    @Inject
    lateinit var viewModel: ListViewModel
    private lateinit var navController: NavController
    private lateinit var mAdapter: SwipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        DaggerAppComponent.factory().create(context!!).inject(this)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_fragmentList.inflateMenu(R.menu.menu_main)
        toolbar_fragmentList.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    logoutUser()
                    navController.navigate(R.id.loginFragment)
                }
            }
            true
        }

        if (isNetworkAvailable()) {
            buttonAdd.setOnClickListener {
                var action = ListFragmentDirections.action_listFragment_to_addReminderFragment()
                action.setItemId(NEW_REMINDER)
                navController.navigate(action)
            }
        }
        initRecyclerView()
        loadData()
    }

    /**
     * ask data from repo, update adapter and schedule notifications.
     */
    private fun loadData() {

        launch {
            mAdapter.setData(ArrayList(viewModel.getData()))
            toolbar_fragmentList.title = "Welcome ${viewModel.getUsername()}"
        }

        viewModel.reminders.observe(viewLifecycleOwner, Observer {
            mAdapter.setData(ArrayList(it))
            scheduleNotification()
        })

    }


    /**
     * override / add - all notification with new items.
     */
    private fun scheduleNotification() {
        context?.let {context->
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val items = mAdapter.items
        for (item in items) {
            val alertIntent = Intent(context, NetworkService.AlertReceiver::class.java)
            alertIntent.apply {
                putExtra("id", item.id)
                putExtra("title", item.title)
                putExtra("body", item.body)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                item.id,
                alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                item.date.time - item.beforeTimeMill,
                pendingIntent
            )
        }
    }}

    /**
     * Cancel all notification in case user logout.
     */
    private fun cancelNotifications() {
        var alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val items = mAdapter.items
        for (item in items) {
            val alertIntent = Intent(context, NetworkService.AlertReceiver::class.java)
            alertIntent.putExtra("id", item.id)
            alertIntent.putExtra("title", item.title)
            alertIntent.putExtra("body", item.body)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                item.id,
                alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(pendingIntent)
        }
    }


    private fun initRecyclerView() {
        mAdapter = SwipeAdapter(context!!, ArrayList())
        recyclerView_reminders?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        enableSwipe()
    }

    private fun enableSwipe() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            /**
             * This method going to called when item swiped away
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedItem = mAdapter.getItem(position)
                // remove item from view
                mAdapter.removeItem(position)
                // remove item from server

                deleteReminder(deletedItem)

                // showing snack bar with undo option
                val view = layout_list
                val snackbar = Snackbar.make(
                    view,
                    " Item removed!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("UNDO") {
                    // When undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, position)
                    addReminder(deletedItem)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }

            private fun deleteReminder(reminder: Reminder) = launch {
                viewModel.deleteReminder(reminder)
            }

            private fun addReminder(reminder: Reminder) = launch {
                viewModel.insertOrUpdate(reminder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val background = ColorDrawable(Color.rgb(146, 0, 0))
                val itemView = viewHolder.itemView

                background.setBounds(
                    0,
                    itemView.top,
                    (itemView.right - dX).toInt(),
                    itemView.bottom
                )
                background.draw(c)
            }
        }
        if (isNetworkAvailable()) {
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView_reminders)
        }

        // add OnItemClickListener
        mAdapter.setOnItemClickListener(object : SwipeAdapter.OnItemClickListener {
            override fun onItemClick(reminder: Reminder, position: Int) {
                if (isNetworkAvailable()) {
                    var action = ListFragmentDirections.action_listFragment_to_addReminderFragment()
                    action.apply {
                        setTitle(reminder.title)
                        setBody(reminder.body)
                        setItemId(reminder.id)
                    }
                    navController.navigate(action)
                }
            }
        })
    }

    private fun logoutUser() = launch {
        cancelNotifications()
        viewModel.logOut()
    }

    fun isNetworkAvailable(): Boolean {
        return viewModel.isNetworkAvailable()

    }


}
