package com.example.nivapptirgul.data.db.network

import androidx.lifecycle.LiveData
import com.example.nivapptirgul.data.db.network.response.UserResponse

interface RemindersNetworkDataSource {
    val downloadedUserData: LiveData<UserResponse>
    val updatesToUser: LiveData<String>
    val isLogged: LiveData<Boolean>


     fun fetchUserData(username: String)

    fun loginUser(username: String)
    fun registerUser(username: String)

    fun logUserIn(username: String)

    fun updateReminders(reminders: String, username: String)

    fun convertDataToObject(data:String): UserResponse

    fun upsertReminder(data:String, username: String)


    fun disconnect()
    fun deleteReminder(convertItemToJson: String?, userName: String)

}