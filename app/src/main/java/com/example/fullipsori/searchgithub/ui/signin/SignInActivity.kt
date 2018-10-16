package com.example.fullipsori.searchgithub.ui.signin

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : AppCompatActivity() {

    internal val api by lazy { provideAuthApi() }
    internal val authTokenProvider by lazy { AuthTokenProvider(this@SignInActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

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


        if(null != authTokenProvider.token){
            Log.d("xsemiyas", "token: ${authTokenProvider.token}")
            launchMainActivity()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val code = intent.data?.getQueryParameter("code") ?: throw IllegalStateException("No code exists")
        getAccessToken(code)
    }

    private fun getAccessToken(code : String){
        api.getAccessToken(BuildConfig.GITHUB_CLIENT_ID, BuildConfig.GITHUB_CLIENT_SECRET, code)
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.accessToken }
                .doOnSubscribe{showProgress(true)}
                .doOnTerminate { showProgress(false) }
                .subscribe(
                        { token ->
                            Log.d("xsemiyas", "getAccessToken: ${token}")
                            authTokenProvider.updateToken(token)
                            launchMainActivity()
                        },
                        {
                            showError(it)
                        }
                )

    }

    private fun showProgress(visiable : Boolean){
        btnActivitySignInStart.visibility = if(visiable) View.GONE else View.VISIBLE
        pbActivitySignIn.visibility = if(visiable) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable){
        Log.d("xsemiyas", throwable.message ?: "no error message")
    }

    private fun launchMainActivity(){
        //check : anko library
        startActivity(intentFor<MainActivity>().clearTask().newTask())
    }
}