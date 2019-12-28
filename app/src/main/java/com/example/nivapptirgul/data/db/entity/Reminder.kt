package com.example.nivapptirgul.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders_table")
data class Reminder(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val title: String,
    val body: String
)