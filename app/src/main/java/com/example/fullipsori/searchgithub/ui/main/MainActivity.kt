package com.example.fullipsori.searchgithub.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnActivityMainSearch.setOnClickListener {
            startActivity(intentFor<SearchActivity>().clearTask().newTask())
        }
    }
}
