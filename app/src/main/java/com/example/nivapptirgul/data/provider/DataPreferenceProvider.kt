package com.example.nivapptirgul.data.provider


interface DataPreferenceProvider{
    fun getUserName(): String
    fun setUserName(username:String)
}