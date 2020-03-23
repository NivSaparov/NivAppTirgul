package com.example.nivapptirgul.data.db.network

import androidx.lifecycle.LiveData
import com.example.nivapptirgul.data.db.network.response.UserResponse

interface RemindersNetworkDataSource {
    val downloadedUserData: LiveData<UserResponse>
    val updatesToUser: LiveData<String>
    val isLogged: LiveData<Boolean>

    suspend fun fetchUserData(username: String)
    suspend fun loginUser(username: String)
    suspend fun registerUser(username: String)
    suspend fun updateReminders(reminders: String, username: String)
    fun convertDataToObject(data: String): UserResponse
    suspend fun upsertReminder(data: String, username: String)
    suspend fun disconnect()
    fun deleteReminder(convertItemToJson: String?, userName: String)
    fun isNetworkAvailable(): Boolean

}