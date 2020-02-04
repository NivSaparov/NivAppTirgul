package com.example.nivapptirgul.data.db.network.response

import com.example.nivapptirgul.data.db.entity.Reminder
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("id")
    var userId: Int = 0,

    @field:SerializedName("username")
    var username: String,

    @field:SerializedName("creationDate")
    var creationDate: String? = null,

    @field:SerializedName("remindersArray")
    val remindersArray: List<Reminder?>? = null
)