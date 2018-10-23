package com.example.fullipsori.searchgithub.di.ui

import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.ui.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SearchModule {
    @Provides
    fun provideSearchViewModelFactory(githubApi: GithubApi, searchHistoryDao: SearchHistoryDao) : SearchViewModelFactory =
            SearchViewModelFactory(githubApi, searchHistoryDao)
}