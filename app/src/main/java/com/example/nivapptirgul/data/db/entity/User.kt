package com.example.nivapptirgul.data.db.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Int = 0,
    var username: String,
    var creationDate: String
)