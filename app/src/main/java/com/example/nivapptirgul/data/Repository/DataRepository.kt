package com.example.nivapptirgul.data.Repository

import androidx.lifecycle.LiveData
import com.example.nivapptirgul.data.db.entity.Reminder

interface DataRepository {
    val userName: LiveData<String>
    val updatesToUser: LiveData<String>
    val isLogged: LiveData<Boolean>
    val userRemindersData: LiveData<List<Reminder>>


    suspend fun updateUserName()
    suspend fun getReminders(): List<Reminder>
    suspend fun insertOrUpdate(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun getReminderById(reminderId:Int):LiveData<Reminder>
    suspend fun loginUser(username: String)
    suspend fun registerUser(username: String)
    suspend fun triggerData()
    suspend fun logOut()
    suspend fun getUsername():String
    suspend fun loginWithoutNetwork():Boolean

    fun isNetworkAvailable():Boolean
}