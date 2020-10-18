package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.notPaging.NotPagingDataSource
import com.like.datasource.sample.data.model.TopArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils
import kotlinx.coroutines.delay

class MemoryTopArticleNotPagingDataSource : NotPagingDataSource<List<TopArticleEntity>?>() {

    override suspend fun load(requestType: RequestType): List<TopArticleEntity>? {
        delay(1000)
        return RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess()
    }

}