package com.example.nivapptirgul.data.db.entity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDao {
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
    fun getItemById(id: Int): Int

    @Transaction
    fun insertOrUpdate(reminder: Reminder) {
        var itemFromDb = getItemById(reminder.id)
        if (itemFromDb == null){
            upsert(reminder)
        }else{
            updateItem(reminder.id, reminder.title, reminder.body)
        }
    }

    @Insert
    @JvmSuppressWildcards
    fun addAll(items: ArrayList<Reminder>)
}