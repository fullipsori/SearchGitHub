package com.example.fullipsori.searchgithub.ui.search
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fullipsori.searchgithub.R
import com.example.fullipsori.searchgithub.api.model.GithubRepo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_repository.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.RepositoryViewHolder>(){

    var searchList : MutableList<GithubRepo> = mutableListOf()

    private var listener : ItemClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RepositoryViewHolder =
            RepositoryViewHolder(p0)

    fun setItems(listRepo: List<GithubRepo>){
        searchList = listRepo.toMutableList()
    }

    override fun getItemCount(): Int = searchList.size


    override fun onBindViewHolder(p0: RepositoryViewHolder, p1: Int) {
        searchList[p1].let {repo ->
            p0.itemView.apply {
                ivItemRepositoryLanguage.text = repo.language ?: "no language"
                ivItemRepositoryNme.text = repo.fullName

                Glide.with(this)
                        .load(repo.owner.avatarUrl)
                        .apply(RequestOptions().placeholder(ColorDrawable(Color.GRAY)))
                        .into(ivItemRepositoryProfile)
                setOnClickListener { listener?.onItemClick(repo) }
            }

        }
    }

    class RepositoryViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false))


    fun setItemClickListener(listener : ItemClickListener?){
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(repository : GithubRepo)
    }
}