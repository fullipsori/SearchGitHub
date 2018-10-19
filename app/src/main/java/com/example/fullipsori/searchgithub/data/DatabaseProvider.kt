package com.example.fullipsori.searchgithub.data

import android.arch.persistence.room.Room
import android.content.Context

private var instance : SearchGithubDatabase? = null

fun provideSearchHistoryDao(context: Context) : SearchHistoryDao
   = provideDatabase(context).searchHistoryDao()

private fun provideDatabase(context: Context) : SearchGithubDatabase
   = instance ?: Room.databaseBuilder(context.applicationContext, SearchGithubDatabase::class.java, "mydb").build()
