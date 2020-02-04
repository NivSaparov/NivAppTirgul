package com.example.nivapptirgul.data.Repository

import androidx.lifecycle.LiveData
import com.example.nivapptirgul.data.db.entity.Reminder

interface DataRepository {
    val userName: LiveData<String>
    val updatesToUser: LiveData<String>
    val isLogged: LiveData<Boolean>
    val userRemindersData: LiveData<List<Reminder>>


    fun updateUserName()
    suspend fun getReminders(): LiveData<List<Reminder>>
    suspend fun insertOrUpdate(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun getReminderById(reminderId:Int):LiveData<Reminder>

    fun loginUser(username: String)
    fun registerUser(username: String)

    fun triggerData()
    fun logOut()
}