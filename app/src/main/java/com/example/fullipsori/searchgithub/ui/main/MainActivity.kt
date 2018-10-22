package com.example.fullipsori.searchgithub.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import com.example.fullipsori.searchgithub.data.SearchHistoryDao
import com.example.fullipsori.searchgithub.data.provideSearchHistoryDao
import com.example.fullipsori.searchgithub.ui.repo.RepositoryActivity
import com.example.fullipsori.searchgithub.ui.search.SearchActivity
import com.example.fullipsori.searchgithub.ui.search.SearchAdapter
import com.example.fullipsori.searchgithub.ui.utils.AutoActivatedDisposable
import com.example.fullipsori.searchgithub.ui.utils.AutoClearedDisposable
import com.example.fullipsori.searchgithub.ui.utils.plusAssign
import com.example.fullipsori.searchgithub.ui.utils.runOnceOnIoScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {

    private val searchAdapter : SearchAdapter by lazy {
        SearchAdapter().apply { setItemClickListener(this@MainActivity) }
    }

    private val viewDisposable = AutoClearedDisposable(this, false)
    private val autoClearedDisposable  = AutoClearedDisposable(this)
    private val searchHistoryDao : SearchHistoryDao by lazy { provideSearchHistoryDao(this) }

    private val viewModelFactory by lazy {
        MainViewModelFactory(provideSearchHistoryDao(this))
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle += AutoActivatedDisposable(this) {
            viewModel.searchHistory
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {items ->
                        with(searchAdapter){
                            if(items.isEmpty){

                            }else {
                                setItems(items.value)
                            }
                            notifyDataSetChanged()
                        }
                    }
        }

        viewDisposable += viewModel.message
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ message ->
                    if(message.isEmpty){

                    }else{

                    }
                }

        lifecycle += viewDisposable
        lifecycle += autoClearedDisposable

        btnActivityMainSearch.setOnClickListener {
            startActivity<SearchActivity>()
        }
/*        btnActivityMainSearch.setOnClickListener {
            startActivity(intentFor<SearchActivity>().clearTask().newTask())
        }*/
        with(rvActivityMainList){
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    override fun onItemClick(repository: GithubRepo) {
        startActivity<RepositoryActivity>(
                RepositoryActivity.KEY_REPO_NAME to repository.name,
                RepositoryActivity.KEY_USER_LOGIN to repository.owner.login
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.menu_activity_main_clear_all == item.itemId){
            autoClearedDisposable += runOnceOnIoScheduler {  searchHistoryDao.clearAll() }
        }
        return super.onOptionsItemSelected(item)
    }

}
