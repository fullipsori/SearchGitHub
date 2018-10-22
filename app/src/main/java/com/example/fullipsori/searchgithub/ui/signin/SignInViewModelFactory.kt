package com.example.fullipsori.searchgithub.ui.signin

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.fullipsori.searchgithub.api.AuthApi
import com.example.fullipsori.searchgithub.data.AuthTokenProvider

class SignInViewModelFactory(val authApi : AuthApi, val authTokenProvider: AuthTokenProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(authApi, authTokenProvider)  as T
    }
}