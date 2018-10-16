package com.example.fullipsori.searchgithub.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private fun provideOkHttpClient(interceptor: Interceptor, authInterceptor: Interceptor?) =
        OkHttpClient.Builder().run{
            if(null != authInterceptor){
                addInterceptor(authInterceptor)
            }
            addInterceptor(interceptor)
            build()
        }

private fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
           setLevel(HttpLoggingInterceptor.Level.BODY)
        }

fun provideAuthApi(): AuthApi
        = Retrofit.Builder()
        .baseUrl("https://github.com")
        .client(provideOkHttpClient(provideLoggingInterceptor(), null))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)