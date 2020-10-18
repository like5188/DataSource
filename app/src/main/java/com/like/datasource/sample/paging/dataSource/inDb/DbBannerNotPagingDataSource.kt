package com.like.datasource.sample.paging.dataSource.inDb

import androidx.fragment.app.FragmentActivity
import androidx.room.withTransaction
import com.like.common.util.Logger
import com.like.common.util.isInternetAvailable
import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDataSource
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.data.model.BannerInfo
import com.like.datasource.sample.data.netWork.RetrofitUtils

class DbBannerNotPagingDataSource(private val activity: FragmentActivity) : NotPagingDataSource<List<BannerInfo>?>() {
    private val db = Db.getInstance(activity.applicationContext)
    private val bannerEntityDao = db.bannerEntityDao()

    override suspend fun load(requestType: RequestType): List<BannerInfo>? {
        val result = if (activity.isInternetAvailable()) {// 有网时
            val data = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
            Logger.d("从网络获取数据：$data")
            if (data != null) {
                db.withTransaction {
                    bannerEntityDao.deleteAll()
                    bannerEntityDao.insertAll(data)
                    Logger.d("更新数据库")
                }
            }
            data
        } else {// 无网时
            val data: List<BannerInfo.BannerEntity>? = bannerEntityDao.getAll()
            Logger.v("从数据库获取数据：$data")
            data
        }
        return if (result.isNullOrEmpty()) {
            null
        } else {
            val bannerInfo = BannerInfo().apply {
                bannerEntities = result
            }
            listOf(bannerInfo)
        }
    }
}