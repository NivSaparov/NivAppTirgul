package com.example.nivapptirgul.ui.fragments.ListFragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.Repository.DataRepositoryImpl
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import com.example.nivapptirgul.data.provider.provideRemindersDao
import com.example.nivapptirgul.ui.ListViewModel
import com.example.nivapptirgul.ui.fragments.ListFragment.recycler.SwipeAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val TITLE_KEY = "title"
const val BODY_KEY = "body"

class ListFragment : Fragment() {


    private lateinit var viewModelFactory: ListViewModelFactory
    private lateinit var viewModel: ListViewModel

    private lateinit var navController: NavController


    // for recycle view
    private lateinit var mAdapter: SwipeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        navController = findNavController()


   }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var dataRepositoryImpl = DataRepositoryImpl(
            DataPreferenceProviderImpl(context!!),
            provideRemindersDao(context!!)
        )

        viewModelFactory = ListViewModelFactory(dataRepositoryImpl)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        mAdapter = SwipeAdapter(context!!, ArrayList())
        updateUserNameFromPreference()
        initRecyclerView()
        loadData()

        buttonAdd.setOnClickListener {
            var action = ListFragmentDirections.action_listFragment_to_addReminderFragment()
            navController.navigate(action)

        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_listFragment.setOnMenuItemClickListener {
            navController.navigate(R.id.open_preference_fragment, null, NavOptions.Builder().setPopUpTo(R.id.addReminderFragment, true).build())
            true
        }

    }

    private fun updateUserNameFromPreference() {
        viewModel.getUserName()
        viewModel.userName.observe(this@ListFragment, Observer { title ->
            toolbar_listFragment.title = title
        })

    }

    private fun initRecyclerView() {
        recyclerView_reminders.apply {
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

                // showing snack bar with undo option
                val snackbar = Snackbar.make(
                    activity!!.window.decorView.rootView,
                    " Item removed!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("UNDO") {
                    // When undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

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

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView_reminders)

        // add OnItemClickListener
        mAdapter.setOnItemClickListener(object : SwipeAdapter.OnItemClickListener {
            override fun onItemClick(reminder: Reminder, position: Int) {
                var action = ListFragmentDirections.action_listFragment_to_addReminderFragment()
                action.setTitle(reminder.title)
                action.setBody(reminder.body)
                action.setItemId(reminder.id)
                navController.navigate(action)
            }
        })
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            val items = viewModel.remindersList.await()
            items.observe(this@ListFragment, Observer {
                mAdapter.setData(ArrayList(items.value))
                Log.d("ListFragment", "update from db, ${items.value}")
            })

        }
    }


    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {

            viewModel.updateEntireReminders(mAdapter.items)
        }
    }

}
