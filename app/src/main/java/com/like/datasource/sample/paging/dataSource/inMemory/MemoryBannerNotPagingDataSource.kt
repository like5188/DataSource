package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDataSource
import com.like.datasource.sample.data.model.BannerInfo
import com.like.datasource.sample.data.netWork.RetrofitUtils
import kotlinx.coroutines.delay

class MemoryBannerNotPagingDataSource : NotPagingDataSource<List<BannerInfo>?>() {

    override suspend fun load(requestType: RequestType): List<BannerInfo>? {
        delay(1000)
        val result = RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess()
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