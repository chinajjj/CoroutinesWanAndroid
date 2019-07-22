package com.kuky.demo.wan.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kuky.demo.wan.android.entity.ArticleDetail

/**
 * @author kuky.
 * @description
 */
class HomeArticleViewModel : ViewModel() {
    var articles: LiveData<PagedList<ArticleDetail>>? = null

    fun fetchHomeArticle() {
        articles = LivePagedListBuilder(
            HomeArticleDataSourceFactory(HomeArticleRepository()),
            PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .build()
        ).build()
    }
}