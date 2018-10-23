package com.example.fullipsori.searchgithub.di.ui

import com.example.fullipsori.searchgithub.api.AuthApi
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import com.example.fullipsori.searchgithub.ui.signin.SignInViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SignInModule {
    @Provides
    fun provideViewModelFactory(authApi: AuthApi, authTokenProvider: AuthTokenProvider) : SignInViewModelFactory =
            SignInViewModelFactory(authApi, authTokenProvider)
}