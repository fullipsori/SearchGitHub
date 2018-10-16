package com.example.fullipsori.searchgithub.api

import com.example.fullipsori.searchgithub.api.model.GithubAccessToken
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

// Retrofit2
// 1. add interface
interface AuthApi {
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
            @Field("client_id") clientId : String,
            @Field("client_secret") clientSecret : String,
            @Field("code") code: String) : Observable<GithubAccessToken>

}

