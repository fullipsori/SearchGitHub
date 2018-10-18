package com.example.fullipsori.searchgithub.api

import android.content.Context
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException

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



internal class AuthIntercerceptor(private val token : String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder().run {
                addHeader("Authorization", "token " + token)
                build()
            }
            proceed(newRequest)
        }
}

private fun provideAuthInterceptor(provider: AuthTokenProvider) : AuthIntercerceptor {
    val token = provider.token ?: throw IllegalArgumentException("token is null")
    return AuthIntercerceptor(token)
}

private fun provideAuthTokenProvider(context : Context)
        = AuthTokenProvider(context.applicationContext)


fun provideGithubApi(context : Context): GithubApi
    = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(provideOkHttpClient(provideLoggingInterceptor(),
                    provideAuthInterceptor(provideAuthTokenProvider(context))))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApi::class.java)