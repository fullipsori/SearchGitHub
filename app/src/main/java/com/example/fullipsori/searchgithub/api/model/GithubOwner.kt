package com.example.fullipsori.searchgithub.api.model

import android.arch.persistence.room.ColumnInfo
import com.google.gson.annotations.SerializedName

class GithubOwner(
        val login: String,
        @SerializedName("avatar_url") @ColumnInfo(name="avatar_url") val avatarUrl : String
)
