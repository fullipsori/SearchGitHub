package com.example.fullipsori.searchgithub.ui.search

import android.arch.lifecycle.ViewModel
import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.ui.utils.SupportOptional
import com.example.fullipsori.searchgithub.ui.utils.emptyOptional
import com.example.fullipsori.searchgithub.ui.utils.optionalOf
import com.example.fullipsori.searchgithub.ui.utils.runOnceOnIoScheduler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SearchViewModel(val githubApi : GithubApi, val searchHistoryDao: SearchHistoryDao) : ViewModel() {
    val searchResult : BehaviorSubject<SupportOptional<List<GithubRepo>>>
            = BehaviorSubject.createDefault(emptyOptional())

    val lastKeyword : BehaviorSubject<SupportOptional<String>> = BehaviorSubject.createDefault(emptyOptional())

    val displayMessage : BehaviorSubject<SupportOptional<String>> =  BehaviorSubject.create()
    val isLoading : BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun searchRepository(query : String) : Disposable = githubApi.searchRepository(query)
            .doOnNext { lastKeyword.onNext(optionalOf(query)) }
            .doOnSubscribe {
                searchResult.onNext(emptyOptional())
                displayMessage.onNext(emptyOptional())
                isLoading.onNext(true)
            }
            .doOnTerminate { isLoading.onNext(false) }
            .flatMap {
                if( 0 == it.totalCount) Observable.error(IllegalStateException("No such result"))
                else Observable.just(it.items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ items ->
                searchResult.onNext(optionalOf(items))
            }){
                displayMessage.onNext(optionalOf(it.message ?: "unknown error"))
            }

    fun addToSearchHistory(repository : GithubRepo) : Disposable =
            runOnceOnIoScheduler { searchHistoryDao.add(repository) }

    fun getSearchHistory() : Disposable =
            runOnceOnIoScheduler { searchHistoryDao.getHistory() }
}