package com.example.fullipsori.searchgithub.di

import com.example.fullipsori.searchgithub.api.AuthApi
import com.example.fullipsori.searchgithub.api.GithubApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class ApiModule {

    @Provides
    fun provideAuthApi(
                    @Named("unauthorized") okHttpClint : OkHttpClient,
                       callAdapter : CallAdapter.Factory,
                       converter : Converter.Factory) : AuthApi
        = Retrofit.Builder()
            .baseUrl("https://github.com")
            .client(okHttpClint)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(converter)
            .build()
            .create(AuthApi::class.java)

    @Provides
    fun provideGithubApi(
                         @Named("authorized") okHttpClint : OkHttpClient,
                         callAdapter: CallAdapter.Factory,
                         converter: Converter.Factory) : GithubApi
        = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClint)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(converter)
            .build()
            .create(GithubApi::class.java)

    @Provides
    fun provideCallAdapterFactory() : CallAdapter.Factory
        = RxJava2CallAdapterFactory.createAsync()

    @Provides
    fun provideConverterFactory() : Converter.Factory
        = GsonConverterFactory.create()

}