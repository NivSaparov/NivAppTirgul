package com.example.nivapptirgul.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "reminders_table")
class Reminder(
    @field:SerializedName("id")
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

    @field:SerializedName("title")
    var title: String,

    @field:SerializedName("body")
    var body: String,

    @field:SerializedName("userId")
    var userId: Int,

    @field:SerializedName("date")
    var date: Date,

    @field:SerializedName("beforeTimeMill")
    var beforeTimeMill: Int


) {
    constructor() : this(0, "", "", 0, Date(),0)

    fun getDateOnlyString():String{
        val format = SimpleDateFormat("M/d/yyyy")
        return format.format(date)
    }

    fun isDone():Boolean{
        if (date.before(Date())){
            return true
        }
        return false
    }


}

