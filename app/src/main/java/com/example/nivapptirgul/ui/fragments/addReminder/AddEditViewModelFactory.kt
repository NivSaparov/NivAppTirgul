package com.example.nivapptirgul.ui.fragments.addReminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nivapptirgul.data.Repository.DataRepository

class AddEditViewModelFactory(
    private val dataRepository: DataRepository
) : ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddEditReminderViewModel(dataRepository) as T
    }
}
