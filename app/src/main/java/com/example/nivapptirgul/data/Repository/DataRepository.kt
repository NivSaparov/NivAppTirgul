package com.example.nivapptirgul.data.Repository

import androidx.lifecycle.LiveData

interface DataRepository {
    var userName: LiveData<String>
    fun updateUserName()
}