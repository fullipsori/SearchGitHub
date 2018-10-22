package com.example.fullipsori.searchgithub.ui.signin

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.fullipsori.searchgithub.BuildConfig
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.provideAuthApi
import com.example.fullipsori.searchgithub.data.AuthTokenProvider
import com.example.fullipsori.searchgithub.ui.main.MainActivity
import com.example.fullipsori.searchgithub.ui.utils.AutoClearedDisposable
import com.example.fullipsori.searchgithub.ui.utils.plusAssign
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : AppCompatActivity() {

    private val disposables = AutoClearedDisposable(this)
    private val viewDisposables = AutoClearedDisposable(this, false)

    private val viewModelFactory by lazy { SignInViewModelFactory(provideAuthApi(), AuthTokenProvider(this)) }
    lateinit var viewModel : SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        lifecycle += disposables
        lifecycle += viewDisposables

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel::class.java)

        btnActivitySignInStart.setOnClickListener {
            val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                    .build()

            // 함수 호출 방밥은?
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this@SignInActivity, authUri)
        }



        viewDisposables += viewModel.accessToken
                .filter{ !it.isEmpty}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ launchMainActivity() }
        viewDisposables += viewModel.errorMessage
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    showError(it)
                }
        viewDisposables += viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {isLoading ->
                    if(isLoading){
                       showProgress(true)
                    }else{
                       showProgress(false)
                    }
                }

        disposables += viewModel.loadAccessToken()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val code = intent.data?.getQueryParameter("code") ?: throw IllegalStateException("No code exists")
        getAccessToken(code)
    }

    private fun getAccessToken(code : String){
        disposables += viewModel.getAccessToken(BuildConfig.GITHUB_CLIENT_ID, BuildConfig.GITHUB_CLIENT_SECRET, code)
    }

    private fun showProgress(visiable : Boolean){
        btnActivitySignInStart.visibility = if(visiable) View.GONE else View.VISIBLE
        pbActivitySignIn.visibility = if(visiable) View.VISIBLE else View.GONE
    }

    private fun showError(message: String){
        Log.d("xsemiyas", message )
    }

    private fun launchMainActivity(){
        //check : anko library
        startActivity(intentFor<MainActivity>().clearTask().newTask())
    }
}