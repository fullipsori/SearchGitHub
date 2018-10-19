package com.example.fullipsori.searchgithub.ui.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

class AutoClearedDisposable (
    private val lifecycleOwner : AppCompatActivity,
    private val alwaysClearOnStop: Boolean = true,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : LifecycleObserver{
    fun add(disposable: Disposable) {
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable += disposable
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun cleanUp(){
        if(!alwaysClearOnStop && !lifecycleOwner.isFinishing) return
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf(){
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}

operator fun AutoClearedDisposable.plusAssign(disposable: Disposable) = this.add(disposable)
operator fun Lifecycle.plusAssign(observer: LifecycleObserver) = this.addObserver(observer)