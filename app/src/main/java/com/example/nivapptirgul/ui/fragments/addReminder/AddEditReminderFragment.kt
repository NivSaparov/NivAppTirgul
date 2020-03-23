package com.example.nivapptirgul.ui.fragments.addReminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.di.DaggerAppComponent
import com.example.nivapptirgul.ui.fragments.ScopedFragment
import kotlinx.android.synthetic.main.add_reminder_fragment.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val NEW_REMINDER = -1
const val TAG = "AddEditFragment"

class AddEditReminderFragment : ScopedFragment() {

    private var reminderItemId = NEW_REMINDER
    @Inject
    lateinit var viewModel: AddEditReminderViewModel
    private lateinit var navController: NavController

    private var myReminder: Reminder = Reminder()

    private var alarmTime = Calendar.getInstance()

    private lateinit var alertTimeBefore: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        context?.let { context ->
            DaggerAppComponent.factory().create(context).inject(this)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_reminder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAddOrEdit()
        initToolbar()
        // Date picker
        val datePickerDialog = DatePickerDialog(
            context,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    alarmTime.set(Calendar.YEAR, year)
                    alarmTime.set(Calendar.MONTH, month)
                    alarmTime.set(Calendar.DAY_OF_MONTH, day)
                    textView_date.text = "$day/${month + 1}/$year"


                }
            }, 0, 0, 0
        )
        datePickerDialog.datePicker.minDate = alarmTime.timeInMillis
        //  "${datePickerDialog.datePicker.dayOfMonth}/${datePickerDialog.datePicker.month + 1}/${datePickerDialog.datePicker.year}"

        button_datePicker.setOnClickListener {
            datePickerDialog.show()
        }

        // Spinner picker
        alertTimeBefore = ArrayAdapter.createFromResource(
            context, R.array.before_array, R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_beforeTime.adapter = adapter
        }

    }

    private fun initToolbar() {
        // When back button pressed
        toolbar_fragmentAddEdit.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        // add toolbar menu
        toolbar_fragmentAddEdit.inflateMenu(R.menu.menu_details_fragment)
        toolbar_fragmentAddEdit.setOnMenuItemClickListener {
            var title = editText_title.text.toString().trim()
            var body = editText_body.text.toString().trim()

            if (title.isEmpty()) {
                editText_title.error = getString(R.string.error_title)
                return@setOnMenuItemClickListener true
            }
            if (body.isEmpty()) {
                editText_body.error = getString(R.string.error_body)
                return@setOnMenuItemClickListener true
            }

            val myDate = alarmTime.time
            myDate.hours = timePicker.currentHour
            myDate.minutes = timePicker.currentMinute
            myDate.seconds = 0


            myReminder.apply {
                this.title = title
                this.body = body
                this.date = date
                this.beforeTimeMill = spinner_beforeTime.selectedItem.toString().toInt()
            }
            if (reminderItemId != NEW_REMINDER) {
                myReminder.id = reminderItemId
            }

            launch {

                viewModel.insertOrUpdate(myReminder!!)

                // when finish, return to listFragment
                navController.navigate(R.id.action_addReminderFragment_to_listFragment)
            }

            true
        }
    }

    private fun initAddOrEdit() = launch {
        // Set view according to request action - Edit or Add
        var data = AddEditReminderFragmentArgs.fromBundle(arguments)
        if (data.itemId == NEW_REMINDER) {
            toolbar_fragmentAddEdit.title = "New Alert"
            timePicker.currentHour = myReminder.date.hours
            timePicker.currentMinute = myReminder.date.minutes
            textView_date.text = myReminder.getDateOnlyString()
        } else {
            toolbar_fragmentAddEdit.title = "Edit Alert"
            val reminder = viewModel.getReminder(data.itemId)
            reminder.observe(this@AddEditReminderFragment, Observer { reminder->
                if (reminder == null) return@Observer
                myReminder = reminder
                reminder.id = data.itemId
                editText_title.setText(reminder.title)
                editText_body.setText(reminder.body)
                reminderItemId = data.itemId
                timePicker.currentHour = myReminder.date.hours
                timePicker.currentMinute = myReminder.date.minutes
                textView_date.text = myReminder.getDateOnlyString()
                spinner_beforeTime.setSelection(alertTimeBefore.getPosition(myReminder.beforeTimeMill.toString()))
            })
        }
    }
}