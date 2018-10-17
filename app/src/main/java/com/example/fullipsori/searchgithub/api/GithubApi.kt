package com.example.fullipsori.searchgithub.api

import com.example.fullipsori.searchgithub.api.model.RepoSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    fun searchRepository(@Query("q") query : String) : Observable<RepoSearchResponse>

}