package com.example.nivapptirgul.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.db.entity.ReminderDao
import com.example.nivapptirgul.data.db.entity.UserDao
import com.example.nivapptirgul.data.provider.DataPreferenceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepositoryImpl(
    private val dataPreferenceProvider: DataPreferenceProvider,
    private val reminderDao: ReminderDao,
    private val userDao: UserDao
) :
    DataRepository {

    var _userName = MutableLiveData<String>()
    override val userName: LiveData<String>
        get() = _userName

    override fun updateUserName() {
        _userName.postValue(dataPreferenceProvider.getUserName())

    }

    override suspend fun getReminders(): LiveData<List<Reminder>> {

        val user = userDao.getUsers().first()
        Log.d(
            "DataRepositories", "User: $user"
        )

        // update
        reminderDao.updateRemindersToUser(user.userId)

        val reminders = reminderDao.getRemindersWithoutLiveData()
        Log.d("DataRepositories ", reminders.toString())

        return withContext(Dispatchers.IO) {
            return@withContext reminderDao.getReminders()
        }
    }

    override suspend fun insertReminder(reminder: Reminder) {
        reminderDao.upsert(reminder)
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    override suspend fun insertOrUpdate(reminder: Reminder) {
        reminderDao.upsert(reminder)
    }

    override suspend fun deleteReminders(reminders: List<Reminder>) {
        for (reminder in reminders) {
            deleteReminder(reminder)
        }
    }

    override suspend fun updateEntireReminders(items: ArrayList<Reminder>) {
        reminderDao.deleteAll()
        reminderDao.addAll(items)

    }
}