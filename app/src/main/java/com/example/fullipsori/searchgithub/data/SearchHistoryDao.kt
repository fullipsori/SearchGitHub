package com.example.fullipsori.searchgithub.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import io.reactivex.Flowable

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(repo: GithubRepo)

    @Query("SELECT * FROM repositories")
    fun getHistory() : Flowable<List<GithubRepo>>

    @Query("DELETE FROM repositories")
    fun clearAll()
}