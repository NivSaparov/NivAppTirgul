package com.example.nivapptirgul.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders_table",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val title: String,
    val body: String,
    val userId: Int? = null
)
