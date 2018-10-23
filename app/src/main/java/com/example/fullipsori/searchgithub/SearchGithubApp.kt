package com.example.fullipsori.searchgithub

import com.example.fullipsori.searchgithub.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SearchGithubApp() : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().papplication(this).build()
    }
}