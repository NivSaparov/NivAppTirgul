package com.example.nivapptirgul.data.provider

import android.content.Context
import com.example.nivapptirgul.data.db.RemindersDatabase


fun provideRemindersDao(context: Context) =
    RemindersDatabase.invoke(context).getReminderDao()


