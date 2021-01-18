package com.like.datasource.sample.paging.dataSource.inDb

import androidx.fragment.app.FragmentActivity
import com.like.common.util.isInternetAvailable
import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDbDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.data.model.ArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils

class DbArticlePagingDataSource(private val activity: FragmentActivity) :
    PageNoKeyedPagingDbDataSource<List<ArticleEntity>?>(MyApplication.PAGE_SIZE) {
    private val db = Db.getInstance(activity.applicationContext)
    private val articleEntityDao = db.articleEntityDao()

    override fun getInitialPage(): Int {
        return 0
    }

    override suspend fun loadFromDb(
        requestType: RequestType,
        pageNo: Int,
        pageSize: Int
    ): List<ArticleEntity>? {
        return articleEntityDao.getPage(pageNo * pageSize, pageSize)
    }

    override fun shouldFetch(requestType: RequestType, resultType: List<ArticleEntity>?): Boolean {
        return activity.isInternetAvailable() && (resultType.isNullOrEmpty() || requestType == RequestType.Refresh)
    }

    override suspend fun fetchFromNetworkAndSaveToDb(
        requestType: RequestType,
        pageNo: Int,
        pageSize: Int
    ) {
        val data = RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
        if (!data.isNullOrEmpty()) {
            if (requestType == RequestType.Refresh) {
                articleEntityDao.deleteAll()
            }
            articleEntityDao.insertAll(data)
        }
    }

}