package com.example.nivapptirgul.data.provider

import android.content.Context
import javax.inject.Inject

const val USER_NAME_KEY = "USER_NAME_KEY"

const val USER_DEFAULT_NAME = "User"


class DataPreferenceProviderImpl @Inject constructor(
    context: Context
) : DataPreferenceProvider, PreferencesProvider(context) {
    override fun getUserName(): String = preferences.getString(USER_NAME_KEY, USER_DEFAULT_NAME)
    override fun setUserName(username: String) {

        preferences.edit().apply {
            putString(USER_NAME_KEY, username)
        }.apply()
    }


}