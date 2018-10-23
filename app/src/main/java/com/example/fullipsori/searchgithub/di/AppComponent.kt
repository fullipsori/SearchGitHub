package com.example.fullipsori.searchgithub.di

import android.app.Application
import com.example.fullipsori.searchgithub.SearchGithubApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, LocalDataModule::class, NetworkModule::class, ApiModule::class,
        AndroidSupportInjectionModule::class,ActivityBinder::class))
interface AppComponent : AndroidInjector<SearchGithubApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun papplication(app : Application) : Builder

        fun build() : AppComponent
    }

}
