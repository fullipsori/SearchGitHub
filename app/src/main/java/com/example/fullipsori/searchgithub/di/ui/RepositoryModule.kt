package com.example.fullipsori.searchgithub.di.ui

import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.ui.repo.RepositoryViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepositoryViewModelFactory(githubApi: GithubApi) : RepositoryViewModelFactory =
            RepositoryViewModelFactory(githubApi)
}