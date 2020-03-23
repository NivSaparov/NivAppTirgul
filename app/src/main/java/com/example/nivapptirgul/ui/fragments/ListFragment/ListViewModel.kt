package com.example.nivapptirgul.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.db.entity.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    internal val userName = dataRepository.userName
    internal val reminders = dataRepository.userRemindersData

    suspend fun getData(): List<Reminder> {
        return withContext(Dispatchers.IO) {
            return@withContext dataRepository.getReminders()
        }
    }

    suspend fun getUsername():String{
        return withContext(Dispatchers.IO){
            return@withContext dataRepository.getUsername()
        }
    }

    suspend fun logOut() {
        dataRepository.logOut()
    }

    suspend fun insertOrUpdate(reminder: Reminder) {
        dataRepository.insertOrUpdate(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        dataRepository.deleteReminder(reminder)
    }
    fun isNetworkAvailable():Boolean{
        return dataRepository.isNetworkAvailable()
    }
}
