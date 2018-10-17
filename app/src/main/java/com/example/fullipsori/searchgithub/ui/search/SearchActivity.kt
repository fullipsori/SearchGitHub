package com.example.fullipsori.searchgithub.ui.search

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.SearchView
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.GithubApi
import com.example.fullipsori.searchgithub.api.provideGithubApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    internal lateinit var menuSearch : MenuItem
    internal lateinit var searchView : SearchView
    internal val api by lazy { provideGithubApi(this@SearchActivity) }

    internal val searchAdapter by lazy {
        SearchAdapter().apply{
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        with(rvActivitySearch){
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.searchAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menuSearch = menu.findItem(R.id.app_bar_search)
        searchView = (menuSearch.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    updateTitle(query)
                    hideSoftKeyboard()
                    collapseSearchView()
                    searchRepository(query)
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
        }

        menuSearch.expandActionView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(R.id.app_bar_search == item?.itemId){
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // private functions
    private fun updateTitle(query: String?){
        supportActionBar?.title = query
    }

    private fun hideSoftKeyboard(){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(searchView.windowToken,0)
        }
    }

    private fun collapseSearchView(){
        menuSearch.collapseActionView()
    }

    private fun searchRepository(query : String){
        api.searchRepository(query)
                .flatMap {
                    if( 0 == it.totalCount){
                        Observable.error(IllegalStateException("count is zero"))
                    }else{
                        Observable.just(it.items)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .doOnTerminate {  }
                .subscribe({items ->
                    with(searchAdapter){
                        setItems(items)
                        notifyDataSetChanged()
                    }
                }){

                }
    }
}