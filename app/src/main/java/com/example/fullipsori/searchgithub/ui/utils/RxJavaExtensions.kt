package com.example.fullipsori.searchgithub.ui.utils

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun runOnceOnIoScheduler(func : () -> Unit) : Disposable
  = Completable.fromCallable(func).subscribeOn(Schedulers.io()).subscribe()