package com.example.fullipsori.searchgithub.ui.repo

import android.arch.lifecycle.ViewModel
import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import com.example.fullipsori.searchgithub.ui.utils.SupportOptional
import com.example.fullipsori.searchgithub.ui.utils.optionalOf
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class RepositoryViewModel(val api : GithubApi) : ViewModel(){
    val repository : BehaviorSubject<SupportOptional<GithubRepo>> = BehaviorSubject.create()
    val message : BehaviorSubject<String> = BehaviorSubject.create()
    val isContentVisible : BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val isLoading : BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun requstRepositoryInfo(login : String, repName: String) :Disposable {
        val repoObservable = if(!repository.hasValue()) {
            api.getRepository(login, repName)
        }else{
            Observable.empty()
        }

        return repoObservable.doOnSubscribe { isLoading.onNext(true) }
                .doOnTerminate { isLoading.onNext(false) }
                .subscribeOn(Schedulers.io())
                .subscribe({ repo ->
                    repository.onNext(optionalOf(repo))
                    isContentVisible.onNext(true)
                },{
                    message.onNext(it.message ?:"unexpected error")
                })
    }
}