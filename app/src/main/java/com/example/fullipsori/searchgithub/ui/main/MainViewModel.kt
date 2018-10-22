package com.example.fullipsori.searchgithub.ui.main

import android.arch.lifecycle.ViewModel
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.ui.utils.*
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class MainViewModel(val searchHistoryDao: SearchHistoryDao) : ViewModel() {

    val message : BehaviorSubject<SupportOptional<String>> = BehaviorSubject.create()

    val searchHistory : Flowable<SupportOptional<List<GithubRepo>>>
        get() = searchHistoryDao.getHistory()
                .map{ optionalOf(it)}
                .doOnNext { optional ->
                    if(optional.value.isEmpty()){
                        message.onNext(optionalOf("No recent repositories"))
                    }else{
                        message.onNext(emptyOptional())
                    }
                }
                .doOnError{
                    message.onNext(optionalOf(it.message ?: "unknown error"))
                }
                .onErrorReturn { emptyOptional() }

    fun clearSearchHistory(): Disposable =
            runOnceOnIoScheduler { searchHistoryDao.clearAll() }
}