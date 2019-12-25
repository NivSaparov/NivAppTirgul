package com.example.nivapptirgul.ui.fragments.ListFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import com.example.nivapptirgul.ui.ListViewModel

class ListViewModelFactory(
    private val dataPreferenceProviderImpl: DataPreferenceProviderImpl
) : ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(dataPreferenceProviderImpl) as T
    }
}

