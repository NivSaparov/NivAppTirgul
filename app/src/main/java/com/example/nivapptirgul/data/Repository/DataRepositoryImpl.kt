package com.example.nivapptirgul.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.db.entity.ReminderDao
import com.example.nivapptirgul.data.db.network.RemindersNetworkDataSource
import com.example.nivapptirgul.data.db.network.response.UserResponse
import com.example.nivapptirgul.data.provider.DataPreferenceProvider
import com.google.gson.Gson
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    private val dataPreferenceProvider: DataPreferenceProvider,
    private val reminderDao: ReminderDao,
    private val remindersNetworkDataSource: RemindersNetworkDataSource
) :
    DataRepository {

    var _userName = MutableLiveData<String>()
    override val userName: LiveData<String>
        get() = _userName


    val _userRemindersData = MutableLiveData<List<Reminder>>()
    override val userRemindersData: LiveData<List<Reminder>>
        get() = reminderDao.getReminders()

    override val isLogged: LiveData<Boolean>
        get() = remindersNetworkDataSource.isLogged

    override val updatesToUser: LiveData<String>
        get() = remindersNetworkDataSource.updatesToUser


    init {
        remindersNetworkDataSource.downloadedUserData.observeForever { newData ->
            // persist user data
            persistUserData(newData)
        }
    }

    /**
     *  Func will be call only when we get update from the server,
     *  that is means that we need to update our local DB
     */
    @ExperimentalCoroutinesApi
    private fun persistUserData(userResponse: UserResponse) {
        // check user response lise of data
        Log.i("DataRepositoryD", "" + userResponse.remindersArray!!.size)
        // We will use GlobalScope because we not depend on the lifecycle of any widget
        GlobalScope.launch {
            (Dispatchers.IO) {

                // first update username in data preference
                dataPreferenceProvider.setUserName(userResponse.username)
                // add all new data to local storage
                reminderDao.updateData(userResponse.remindersArray as List<Reminder>)

            }
        }
    }


    override suspend fun triggerData() {
        // ask server for data
        remindersNetworkDataSource.fetchUserData(dataPreferenceProvider.getUserName())
    }

    override suspend fun getReminders(): List<Reminder> {
        return withContext(Dispatchers.IO) {
            return@withContext reminderDao.getRemindersWithoutLiveData()
        }
    }

    override suspend fun getReminderById(reminderId: Int): LiveData<Reminder> {
        return withContext(Dispatchers.IO) {
            return@withContext reminderDao.getItemById(reminderId)
        }
    }


    override suspend fun insertOrUpdate(reminder: Reminder) {
        remindersNetworkDataSource.upsertReminder(
            convertItemToJson(reminder),
            dataPreferenceProvider.getUserName()
        )
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        remindersNetworkDataSource.deleteReminder(
            convertItemToJson(reminder),
            dataPreferenceProvider.getUserName()
        )
    }

    override suspend fun updateUserName() {
        _userName.postValue(dataPreferenceProvider.getUserName())

    }

    override suspend fun loginUser(username: String) {
        dataPreferenceProvider.setUserName(username)
        remindersNetworkDataSource.loginUser(username)
    }

    override suspend fun registerUser(username: String) {
        remindersNetworkDataSource.registerUser(username)
    }


    private fun convertItemToJson(reminder: Reminder) = Gson().toJson(reminder)

    override suspend fun logOut() {
        dataPreferenceProvider.setUserName("")
        remindersNetworkDataSource.disconnect()
    }

    override suspend fun getUsername(): String {
        return dataPreferenceProvider.getUserName()
    }

    override suspend fun loginWithoutNetwork(): Boolean {
        if (reminderDao.getRemindersWithoutLiveData().isEmpty() || getUsername() == "") {
            return false
        } else {
            remindersNetworkDataSource.loginUser(getUsername())
        }
        return true

    }

    override fun isNetworkAvailable(): Boolean {
        return remindersNetworkDataSource.isNetworkAvailable()
    }
}
