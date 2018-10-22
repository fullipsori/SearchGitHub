package com.example.fullipsori.searchgithub.ui.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.data.SearchHistoryDao

class SearchViewModelFactory(val githubApi: GithubApi, val searchHistoryDao: SearchHistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(githubApi, searchHistoryDao) as T
    }
}