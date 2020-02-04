package com.example.nivapptirgul.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    var updatesToUser = dataRepository.updatesToUser
    var isLoggedIn = dataRepository.isLogged

    fun login(username: String) {
        dataRepository.loginUser(username)
    }

    fun register(username: String) {
        dataRepository.registerUser(username)
    }

}
