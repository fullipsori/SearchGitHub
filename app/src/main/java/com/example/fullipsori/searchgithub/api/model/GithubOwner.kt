package com.example.fullipsori.searchgithub.api.model

import com.google.gson.annotations.SerializedName

class GithubOwner(
        val login: String,
        @SerializedName("avatar_url") val avatarUrl : String
)
