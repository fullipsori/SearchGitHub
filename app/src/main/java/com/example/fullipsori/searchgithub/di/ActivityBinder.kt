package com.example.fullipsori.searchgithub.di

import com.example.fullipsori.searchgithub.di.ui.MainModule
import com.example.fullipsori.searchgithub.di.ui.RepositoryModule
import com.example.fullipsori.searchgithub.di.ui.SearchModule
import com.example.fullipsori.searchgithub.di.ui.SignInModule
import com.example.fullipsori.searchgithub.ui.main.MainActivity
import com.example.fullipsori.searchgithub.ui.repo.RepositoryActivity
import com.example.fullipsori.searchgithub.ui.search.SearchActivity
import com.example.fullipsori.searchgithub.ui.signin.SignInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinder{
    @ContributesAndroidInjector (modules= arrayOf(SignInModule::class))
    abstract fun bindSignInActivity() : SignInActivity

    @ContributesAndroidInjector (modules = arrayOf(MainModule::class))
    abstract fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector (modules = arrayOf(SearchModule::class))
    abstract fun bindSearchActivity() : SearchActivity

    @ContributesAndroidInjector (modules = arrayOf(RepositoryModule::class))
    abstract fun bindRepositoryActivity() : RepositoryActivity

}