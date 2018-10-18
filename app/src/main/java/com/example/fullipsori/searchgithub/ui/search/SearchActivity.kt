package com.example.fullipsori.searchgithub.ui.search

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import com.example.fullipsori.searchgithub.api.provideGithubApi
import com.example.fullipsori.searchgithub.ui.repo.RepositoryActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity

class SearchActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {
    internal lateinit var menuSearch : MenuItem
    internal lateinit var searchView : SearchView
    internal val api by lazy { provideGithubApi(this@SearchActivity) }

    internal val searchAdapter by lazy {
        SearchAdapter().apply{
            setItemClickListener(this@SearchActivity)
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
                override fun onQueryTextSubmit(query: String): Boolean {
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

    override fun onItemClick(repository: GithubRepo) {
        startActivity<RepositoryActivity>(
                RepositoryActivity.KEY_USER_LOGIN to repository.owner.login,
                RepositoryActivity.KEY_REPO_NAME to repository.name)
    }
}