package com.example.fullipsori.searchgithub.ui.signin

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.fullipsori.searchgithub.BuildConfig
import com.example.fullipsori.searchgithub.api.AuthApi
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import com.example.fullipsori.searchgithub.ui.utils.SupportOptional
import com.example.fullipsori.searchgithub.ui.utils.optionalOf
import com.example.fullipsori.searchgithub.ui.utils.plusAssign
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SignInViewModel(val authApi : AuthApi, val authTokenProvider: AuthTokenProvider) : ViewModel() {

    val accessToken : BehaviorSubject<SupportOptional<String>> = BehaviorSubject.create()
    val isLoading : BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val errorMessage : PublishSubject<String> = PublishSubject.create()

    fun getAccessToken(clientId : String, clientSecret : String, code : String) : Disposable =
        authApi.getAccessToken(clientId, clientSecret, code)
                .observeOn(AndroidSchedulers.mainThread())
                .map{it.accessToken}
                .doOnSubscribe { isLoading.onNext(true) }
                .doOnTerminate { isLoading.onNext(false) }
                .subscribe({token ->
                    authTokenProvider.updateToken(token)
                    accessToken.onNext(optionalOf(token))
                },{
                    errorMessage.onNext(it.message ?: "unknown error")
                })

    fun loadAccessToken() : Disposable =
            Single.fromCallable { optionalOf(authTokenProvider.token) }
                    .subscribeOn(Schedulers.io())
                    .subscribe ( Consumer<SupportOptional<String>>{
                        accessToken.onNext(it)
                    } )

}