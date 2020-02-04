package com.example.nivapptirgul.data.db.entity

import androidx.lifecycle.LiveData
import androidx.room.*
import javax.inject.Inject

@Dao
interface ReminderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(reminder: Reminder)

    @Query("UPDATE reminders_table SET title = :title AND body = :body WHERE id = :id")
    fun updateItem(id: Int, title: String, body: String)

    @Query("SELECT * FROM reminders_table ORDER by id desc")
    fun getReminders(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminders_table ORDER by id desc")
    fun getRemindersWithoutLiveData(): List<Reminder>

    @Delete
    fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminders_table")
    fun deleteAll()

    @Query("SELECT * FROM reminders_table WHERE id = :id")
    fun getItemById(id: Int): LiveData<Reminder>

    @Transaction
    fun insertOrUpdate(reminder: Reminder) {
        var itemFromDb = getItemById(reminder.id)
        if (itemFromDb == null) {
            upsert(reminder)
        } else {
            updateItem(reminder.id, reminder.title, reminder.body)
        }
    }

    @Transaction
    fun updateData(data: List<Reminder>){
        deleteAll()
        addAll(data)

    }

    @Insert
    fun addAll(items: List<Reminder>)

    @Query("UPDATE reminders_table SET userId = :id WHERE  userId IS NULL ")
    fun updateRemindersToUser(id: Int)
}