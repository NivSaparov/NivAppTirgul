package com.example.nivapptirgul.ui.fragments.addReminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.Repository.DataRepositoryImpl
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import com.example.nivapptirgul.data.provider.provideRemindersDao
import com.example.nivapptirgul.data.provider.provideUserDao
import kotlinx.android.synthetic.main.add_reminder_fragment.*
import kotlinx.coroutines.*

const val NEW_REMINDER = -1
const val NULL_BUNDLE = "@null"

class AddEditReminderFragment : Fragment() {

    private var reminderItemId = NEW_REMINDER

    private lateinit var viewModel: AddEditReminderViewModel
    private lateinit var viewModelFactory: AddEditViewModelFactory
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_reminder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var dataRepositoryImpl = DataRepositoryImpl(
            DataPreferenceProviderImpl(context!!),
            provideRemindersDao(context!!),
            provideUserDao(context !!)
        )

        viewModelFactory = AddEditViewModelFactory(dataRepositoryImpl)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddEditReminderViewModel::class.java)

        // save data and go to listFragment
        buttonCheck.setOnClickListener {
            var title = editText_title.text.toString().trim()
            var body = editText_body.text.toString().trim()

            if (title.isEmpty()) {
                editText_title.error = "title required"
                return@setOnClickListener
            }
            if (body.isEmpty()) {
                editText_body.error = "set reminder"
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO) {
                var reminder = Reminder(title = title, body = body)
                if (reminderItemId != NEW_REMINDER){
                    reminder.id = reminderItemId
                }
                val insert = async {  viewModel.insertOrUpdate(reminder)}
                insert.await()

                // when finish, return to listFragment
                navController.navigate(R.id.action_addReminderFragment_to_listFragment)

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set view according to request action - Edit OR Add
        var data = AddEditReminderFragmentArgs.fromBundle(arguments)
        if (data.title != NULL_BUNDLE) {
            // set toolbar title
            toolbar_addEditFragment.title = "Edit Reminder"
            // set views from arguments
            editText_title.setText(data.title)
            editText_body.setText(data.body)
            reminderItemId = data.itemId
        } else {
            // set toolbar title
            reminderItemId = NEW_REMINDER
            toolbar_addEditFragment.title = "Add Reminder"

        }
    }
}

