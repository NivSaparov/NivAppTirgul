package com.example.nivapptirgul.ui.fragments.addReminder

import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.db.entity.Reminder

class AddEditReminderViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {

    suspend fun insertReminder(reminder: Reminder) {

        dataRepository.insertReminder(reminder)
    }

    suspend fun insertOrUpdate(reminder: Reminder){
        dataRepository.insertOrUpdate(reminder)
    }

}
