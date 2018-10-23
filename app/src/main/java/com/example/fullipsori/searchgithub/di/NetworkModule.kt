package com.example.fullipsori.searchgithub.di

import com.example.fullipsori.searchgithub.AuthInterceptor
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Named("unauthorized")
    @Singleton
    fun provideUnauthorizedOkHttpclient(
            loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient
    = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    @Named("authorized")
    @Singleton
    fun provideAuthoirzedOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authIntercerceptor: AuthInterceptor
    ) : OkHttpClient
    = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authIntercerceptor)
            .build()

    @Provides
    @Singleton
    fun provideLogginInterceptor() : HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

    @Provides
    @Singleton
    fun provideAuthInterceptor(provider : AuthTokenProvider) : AuthInterceptor {
        val token = provider.token ?: throw IllegalStateException("authToken cannot be null")
        return AuthInterceptor(token)
    }

}