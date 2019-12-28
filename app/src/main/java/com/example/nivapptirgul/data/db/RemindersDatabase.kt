package com.example.nivapptirgul.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.db.entity.ReminderDao

@Database(
    entities = [Reminder::class],
    version = 1
)
abstract class RemindersDatabase: RoomDatabase(){

    abstract fun getReminderDao(): ReminderDao

    /**
     *  Create this db once,
     *  and lock it each time -> thread safety
     */
    companion object{
        @Volatile
        private var instance: RemindersDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RemindersDatabase::class.java,
                "reminder.db"
            ).build()
    }
}