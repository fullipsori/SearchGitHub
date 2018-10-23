package com.example.fullipsori.searchgithub.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Named("appContext")
    @Singleton
    fun provideContxt(application: Application) : Context =
            application.applicationContext
}