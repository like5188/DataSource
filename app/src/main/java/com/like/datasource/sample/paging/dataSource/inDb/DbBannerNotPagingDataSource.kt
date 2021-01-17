package com.like.datasource.sample.paging.dataSource.inDb

import androidx.fragment.app.FragmentActivity
import com.like.common.util.isInternetAvailable
import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDbDataSource
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.data.model.BannerInfo
import com.like.datasource.sample.data.netWork.RetrofitUtils

class DbBannerNotPagingDataSource(private val activity: FragmentActivity) :
    NotPagingDbDataSource<List<BannerInfo>?>() {
    private val db = Db.getInstance(activity.applicationContext)
    private val bannerEntityDao = db.bannerEntityDao()

    override suspend fun loadFromDb(requestType: RequestType): List<BannerInfo>? {
        val data = bannerEntityDao.getAll()
        if (data.isEmpty()) {
            return null
        }
        val bannerInfo = BannerInfo().apply {
            bannerEntities = data
        }
        return listOf(bannerInfo)
    }

    override fun shouldFetch(requestType: RequestType, resultType: List<BannerInfo>?): Boolean {
        return activity.isInternetAvailable() && (resultType.isNullOrEmpty() || requestType == RequestType.Refresh)
    }

    override suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType) {
        val data = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
        if (data.isNullOrEmpty()) {
            throw RuntimeException("data is null or empty")
        } else {
            bannerEntityDao.insertAll(data)
        }
    }
}