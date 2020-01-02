package com.example.nivapptirgul.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.data.db.entity.ReminderDao
import com.example.nivapptirgul.data.db.entity.User
import com.example.nivapptirgul.data.db.entity.UserDao
import java.util.*

@Database(
    entities = arrayOf((Reminder::class), (User::class)),
    version = 3
)
abstract class RemindersDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao
    abstract fun getUserDao(): UserDao


    /**
     *  Create this db once,
     *  and lock it each time -> thread safety
     */
    companion object {
        @Volatile
        private var instance: RemindersDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context): RemindersDatabase {
            // Migration from version 1 to version 2
            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Create user database
                    database.execSQL(
                        "CREATE TABLE `users_table`" +
                                "(`userId` INTEGER NOT NULL," +
                                "`username` TEXT NOT NULL," +
                                "`creationDate` TEXT NOT NULL, " +
                                "PRIMARY KEY(`userId`))"
                    )
                }
            }


            val MIGRATION_2_3 = object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    //get current date
                    var date = Calendar.getInstance().time.toString()

                    // create new user
                    database.execSQL(
                        "INSERT INTO 'users_table' ('username','creationDate') VALUES('users','$date')"
                    )

                    // create temporary reminders table
                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS 'reminders_new'" +
                                "('title' TEXT NOT NULL," +
                                "'body' TEXT NOT NULL," +
                                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "'userId' INTEGER," +
                                "FOREIGN KEY(`userId`) REFERENCES `users_table`(`userId`) ON UPDATE NO ACTION ON DELETE CASCADE)"
                    )



                    // copy data from remainders_table to reminder_new
                    database.execSQL("INSERT INTO 'reminders_new' ('title', 'body', 'userId') SELECT 'r.title', 'r.body', 'u.userId' FROM 'reminders_table' AS r JOIN 'users_table' as u")

                    // Remove old table
                    database.execSQL("DROP TABLE 'reminders_table'")

                    // Rename new table
                    database.execSQL("ALTER TABLE 'reminders_new' RENAME TO 'reminders_table'")

                }
            }

            // Create new database with migration in case user have older database schema
            return Room.databaseBuilder(
                context.applicationContext,
                RemindersDatabase::class.java,
                "reminder.db")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }


}

