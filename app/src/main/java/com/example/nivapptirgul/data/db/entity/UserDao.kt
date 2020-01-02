package com.example.nivapptirgul.data.db.entity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User)

    @Query("SELECT username FROM users_table WHERE userId = :id")
    fun getUserNameById(id: Int): String

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM reminders_table WHERE userId = :userId")
    fun getUserReminders(userId: String): List<Reminder>

    @Query("SELECT * FROM users_table")
    fun getUsers():List<User>


}