package com.example.nivapptirgul.ui.fragments.addReminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.db.entity.Reminder
import javax.inject.Inject

class AddEditReminderViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    suspend fun insertOrUpdate(reminder: Reminder) {
        dataRepository.insertOrUpdate(reminder)
    }

    suspend fun getReminder(id: Int): LiveData<Reminder> {
        return dataRepository.getReminderById(id)
    }

}
