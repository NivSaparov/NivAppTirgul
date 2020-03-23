package com.example.nivapptirgul.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.example.nivapptirgul.data.Repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    var updatesToUser = dataRepository.updatesToUser
    var isLoggedIn = dataRepository.isLogged

    fun login(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            dataRepository.loginUser(username)
        }
    }

    fun register(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            dataRepository.registerUser(username)
        }
    }

    suspend fun loginWithoutNetwork(): Boolean {
        return withContext(Dispatchers.IO) {
            dataRepository.loginWithoutNetwork()
        }
    }
}
