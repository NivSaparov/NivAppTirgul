package com.example.nivapptirgul.data.db.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nivapptirgul.data.db.network.response.UserResponse
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import java.net.URISyntaxException

const val MESSAGES = "MESSAGES"
const val LOGIN = "LOGIN"
const val REGISTER = "REGISTER"
const val DATA_FROM_CLIENT = "DATA_FROM_CLIENT"
const val GET_DATA = "DATA_FROM_SERVER"
const val ASK_FOR_DATA = "ASK_FOR_DATA"
const val DELETE_ITEM = "DELETE_ITEM"

const val TAG = "DataSource"

class RemindersNetworkDataSourceImpl : RemindersNetworkDataSource {
    private val _downloadedUserData = MutableLiveData<UserResponse>()
    override val downloadedUserData: LiveData<UserResponse>
        get() = _downloadedUserData

    private val _updatesToUser = MutableLiveData<String>()
    override val updatesToUser: LiveData<String>
        get() = _updatesToUser


    private val _isLogged = MutableLiveData<Boolean>()
    override val isLogged: LiveData<Boolean>
        get() = _isLogged

    private lateinit var mSocket: Socket

    init {
        initSocket()
        handleSocketCallbacks()
    }

    private fun initSocket() {

        try {
            mSocket = IO.socket("http://192.168.122.1:3002")
            mSocket.connect()
        } catch (e: URISyntaxException) {
            Log.i(TAG, e.message)
        }

    }

    private fun handleSocketCallbacks() {
        // Messages from server
        mSocket.on(MESSAGES) { msgFromServer ->
            _updatesToUser.postValue(msgFromServer.first().toString())
            Log.i(TAG, "Message - : ${msgFromServer.first().toString()}")
        }

        mSocket.on(LOGIN) { dataFromServer ->
            when (dataFromServer.first()) {
                true -> {
                    // convert user json to object and post it.
                    val userData = convertDataToObject(dataFromServer.elementAt(1).toString())
                    _downloadedUserData.postValue(userData)
                    _isLogged.postValue(true)

                }
            }
        }


        // When server update new data
        mSocket.on(GET_DATA) { dataFromServer ->
            // convert user json to object and post it.
            val userData = convertDataToObject(dataFromServer.first().toString())
            Log.i(TAG, "Update from server")
            _downloadedUserData.postValue(userData)
            _isLogged.postValue(true)
        }

        // HANDLE ERRORS
        mSocket.on(Socket.EVENT_CONNECT_ERROR) {
            _updatesToUser.postValue("Please check your internet connection")
            mSocket.close()
        }
    }

    override fun deleteReminder(convertItemToJson: String?, userName: String) {
        mSocket.emit(DELETE_ITEM, convertItemToJson, userName)
    }

    override suspend fun fetchUserData(username: String) {
        mSocket.emit(ASK_FOR_DATA, username)
    }

    override suspend fun loginUser(username: String) {
        mSocket.emit(LOGIN, username)
    }

    override suspend fun registerUser(username: String) {
        mSocket.emit(REGISTER, username)
    }

    override suspend fun updateReminders(reminders: String, username: String) {
        mSocket.emit(DATA_FROM_CLIENT, reminders, username)
    }

    override suspend fun upsertReminder(data: String, username: String) {
        mSocket.emit(DATA_FROM_CLIENT, data, username)
    }

    override suspend fun disconnect() {
        mSocket.disconnect()
    }

    override fun convertDataToObject(data: String): UserResponse {
        return Gson().fromJson(data, UserResponse::class.java)
    }

    override fun isNetworkAvailable(): Boolean {
        return mSocket.connected()
    }
}

