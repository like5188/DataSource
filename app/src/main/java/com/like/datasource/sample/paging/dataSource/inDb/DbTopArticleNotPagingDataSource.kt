package com.like.datasource.sample.paging.dataSource.inDb

import androidx.fragment.app.FragmentActivity
import androidx.room.withTransaction
import com.like.common.util.Logger
import com.like.common.util.isInternetAvailable
import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDataSource
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.data.model.TopArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils

class DbTopArticleNotPagingDataSource(private val activity: FragmentActivity) : NotPagingDataSource<List<TopArticleEntity>?>() {
    private val db = Db.getInstance(activity.applicationContext)
    private val topArticleEntityDao = db.topArticleEntityDao()

    override suspend fun load(requestType: RequestType): List<TopArticleEntity>? {
        return if (activity.isInternetAvailable()) {// 有网时
            val data = RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
            Logger.d("从网络获取数据：$data")
            if (data != null) {
                db.withTransaction {
                    topArticleEntityDao.deleteAll()
                    topArticleEntityDao.insertAll(data)
                    Logger.d("更新数据库")
                }
            }
            data
        } else {// 无网时
            val data: List<TopArticleEntity>? = topArticleEntityDao.getAll()
            Logger.v("从数据库获取数据：$data")
            data
        }
    }
}