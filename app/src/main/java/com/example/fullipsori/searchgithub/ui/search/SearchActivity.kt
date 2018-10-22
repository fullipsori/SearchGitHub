package com.example.fullipsori.searchgithub.ui.search

import android.arch.lifecycle.ViewModelProviders
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
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.data.provideSearchHistoryDao
import com.example.fullipsori.searchgithub.ui.repo.RepositoryActivity
import com.example.fullipsori.searchgithub.ui.utils.AutoClearedDisposable
import com.example.fullipsori.searchgithub.ui.utils.plusAssign
import com.example.fullipsori.searchgithub.ui.utils.runOnceOnIoScheduler
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity

class SearchActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {
    private lateinit var menuSearch : MenuItem
    private lateinit var searchView : SearchView
    private val api by lazy { provideGithubApi(this@SearchActivity) }
    private val disposables = AutoClearedDisposable(this)
    private val viewDisposable = AutoClearedDisposable(this, alwaysClearOnStop = false)

    private val viewModelFactory by lazy {
        SearchViewModelFactory(provideGithubApi(this), provideSearchHistoryDao(this@SearchActivity))
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SearchViewModel::class.java]
    }

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

        viewDisposable += viewModel.searchResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ message ->
                    with(searchAdapter){
                        if(message.isEmpty){
                            setItems(listOf())
                        } else {
                            setItems(message.value)
                        }
                        notifyDataSetChanged()
                    }
                }
        viewDisposable += viewModel.displayMessage
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it.isEmpty){
                        hideError()
                    }else{
                        showError(it.value)
                    }
                }

        viewDisposable += viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    if (it){
                        showProgress()
                    }else{
                        hideProgress()
                    }
                }


        lifecycle += disposables
        lifecycle += viewDisposable

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menuSearch = menu.findItem(R.id.app_bar_search)
        searchView = (menuSearch.actionView as SearchView)

        viewDisposable += viewModel.lastKeyword
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { keyword ->
                    if(keyword.isEmpty){
                        menuSearch.expandActionView()
                    }else{
                        updateTitle(keyword.value)
                    }
                }

        viewDisposable += searchView.queryTextChangeEvents()
                .filter { it.isSubmitted }
                .map { it.queryText().toString() }
                .filter { it.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    updateTitle(query)
                    hideSoftKeyboard()
                    collapseSearchView()
                    searchRepository(query)
                },{})

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(R.id.app_bar_search == item?.itemId){
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideError() = Unit

    private fun showError(message : String) = Unit

    private fun showProgress() = Unit
    private fun hideProgress() = Unit

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
        disposables += viewModel.searchRepository(query)
    }

    override fun onItemClick(repository: GithubRepo) {
        disposables += viewModel.addToSearchHistory(repository)
        startActivity<RepositoryActivity>(
                RepositoryActivity.KEY_USER_LOGIN to repository.owner.login,
                RepositoryActivity.KEY_REPO_NAME to repository.name)
    }

}