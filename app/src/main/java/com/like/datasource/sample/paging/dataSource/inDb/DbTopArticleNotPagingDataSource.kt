package com.like.datasource.sample.paging.dataSource.inDb

import androidx.fragment.app.FragmentActivity
import com.like.common.util.isInternetAvailable
import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDbDataSource
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.data.model.TopArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils

class DbTopArticleNotPagingDataSource(private val activity: FragmentActivity) :
    NotPagingDbDataSource<List<TopArticleEntity>?>() {
    private val db = Db.getInstance(activity.applicationContext)
    private val topArticleEntityDao = db.topArticleEntityDao()

    override suspend fun loadFromDb(requestType: RequestType): List<TopArticleEntity>? {
        return topArticleEntityDao.getAll()
    }

    override fun shouldFetch(requestType: RequestType, resultType: List<TopArticleEntity>?): Boolean {
        return activity.isInternetAvailable() && (resultType.isNullOrEmpty() || requestType == RequestType.Refresh)
    }

    override suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType) {
        val data = RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
        if (!data.isNullOrEmpty()) {
            topArticleEntityDao.insertAll(data)
        }
    }

}