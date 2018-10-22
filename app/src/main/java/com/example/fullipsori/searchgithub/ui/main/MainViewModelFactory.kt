package com.example.fullipsori.searchgithub.ui.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.fullipsori.searchgithub.data.SearchHistoryDao

class MainViewModelFactory(val searchHistoryDao: SearchHistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(searchHistoryDao) as T
    }
}