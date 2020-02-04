package com.example.nivapptirgul.ui

import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.db.entity.Reminder
import javax.inject.Inject

class ListViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    internal val userName = dataRepository.userName
    internal val reminders = dataRepository.userRemindersData

    fun getData() {
        dataRepository.triggerData()

    }

    fun logOut() {
        dataRepository.logOut()
    }

    suspend fun insertOrUpdate(reminder: Reminder) {
        dataRepository.insertOrUpdate(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        dataRepository.deleteReminder(reminder)
    }
}
