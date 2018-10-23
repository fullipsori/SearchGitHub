package com.example.fullipsori.searchgithub.di.ui

import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.ui.main.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun provideMainModuleFactory(searchHistoryDao: SearchHistoryDao) : MainViewModelFactory =
            MainViewModelFactory(searchHistoryDao)
}