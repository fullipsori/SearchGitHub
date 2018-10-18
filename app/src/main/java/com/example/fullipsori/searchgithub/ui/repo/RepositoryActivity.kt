package com.example.fullipsori.searchgithub.ui.repo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.provideGithubApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_repository.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.IllegalArgumentException

class RepositoryActivity : AppCompatActivity() {
    companion object {
        const val KEY_USER_LOGIN = "user_login"
        const val KEY_REPO_NAME = "repo_name"
    }

    val api by lazy { provideGithubApi(this@RepositoryActivity) }

    val dateFormatInResponse : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
    val dateFormatToShow : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        val login = intent.getStringExtra(KEY_USER_LOGIN) ?: throw IllegalArgumentException("user login info")
        val repo = intent.getStringExtra(KEY_REPO_NAME) ?: throw IllegalArgumentException("key repo name")

        showRepositoryInfo(login, repo)
    }


    private fun showRepositoryInfo(login : String, repo : String){
        api.getRepository(login, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .doOnComplete {  }
                .doOnError {  }
                .subscribe({repo ->
                   Glide.with(this@RepositoryActivity)
                           .load(repo.owner.avatarUrl)
                           .into(ivActivityRepositoryProfile)
                    tvActivityRepositoryName.text = repo.fullName
                    tvActivityRepositoryStars.text = resources.getQuantityString(R.plurals.star, repo.stars, repo.stars)
                    tvActivityRepositoryDescription.text = repo.description ?: "No description provided"
                    tvActivityRepositoryLanguage.text = repo.language ?: "No language description"

                    try{
                        val lastUpdate = dateFormatInResponse.parse(repo.updatedAt)
                        tvActivityRepositoryLastUpdate.text = dateFormatToShow.format(lastUpdate)
                    } catch (e: ParseException){
                        tvActivityRepositoryLastUpdate.text = "Unknown"
                    }
                },{

                })

    }
}