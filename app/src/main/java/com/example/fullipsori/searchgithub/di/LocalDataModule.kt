package com.example.fullipsori.searchgithub.di

import android.arch.persistence.room.Room
import android.content.Context
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import com.example.fullipsori.searchgithub.data.SearchGithubDatabase
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class LocalDataModule {
    @Provides
    @Singleton
    fun provideAuthTokenProvider(@Named("appContext") context : Context) : AuthTokenProvider =
            AuthTokenProvider(context)

    @Provides
    @Singleton
    fun provideSearchHistoryDao(db : SearchGithubDatabase) : SearchHistoryDao =
            db.searchHistoryDao()

    @Provides
    @Singleton
    fun provideDatabase(@Named("appContext") context : Context) : SearchGithubDatabase =
            Room.databaseBuilder(context,SearchGithubDatabase::class.java, "myDB").build()
}