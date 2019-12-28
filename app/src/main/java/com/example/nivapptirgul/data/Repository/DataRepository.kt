package com.example.nivapptirgul.data.Repository

import androidx.lifecycle.LiveData
import com.example.nivapptirgul.data.db.entity.Reminder

interface DataRepository {
    val userName: LiveData<String>
    fun updateUserName()
    suspend fun getReminders(): LiveData<List<Reminder>>

    suspend fun insertReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)

    suspend fun insertOrUpdate(reminder: Reminder)

    suspend fun deleteReminders(reminder: List<Reminder>)

    suspend fun updateEntireReminders(items: ArrayList<Reminder>)

}