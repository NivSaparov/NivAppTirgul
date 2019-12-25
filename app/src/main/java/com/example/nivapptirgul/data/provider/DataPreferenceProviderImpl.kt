package com.example.nivapptirgul.data.provider

import android.content.Context

const val USER_NAME_KEY = "USER_NAME_KEY"

const val USER_DEFAULT_NAME = "User"


class DataPreferenceProviderImpl(
    context: Context
) : DataPreferenceProvider, PreferencesProvider(context) {
    override fun getUserName(): String = preferences.getString(USER_NAME_KEY, USER_DEFAULT_NAME)
}