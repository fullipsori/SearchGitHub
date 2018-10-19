package com.example.fullipsori.searchgithub.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Entity
import android.arch.persistence.room.RoomDatabase
import com.example.fullipsori.searchgithub.api.model.GithubRepo

@Database(entities = arrayOf(GithubRepo::class), version=1)
abstract class SearchGithubDatabase : RoomDatabase() {
    abstract fun searchHistoryDao() : SearchHistoryDao
}