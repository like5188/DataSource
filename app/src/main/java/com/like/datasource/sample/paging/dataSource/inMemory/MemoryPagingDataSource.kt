package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.util.MultiDataSourceHelper

class MemoryPagingDataSource(
    private val memoryBannerNotPagingDataSource: MemoryBannerNotPagingDataSource,
    private val memoryTopArticleNotPagingDataSource: MemoryTopArticleNotPagingDataSource,
    private val memoryArticlePagingDataSource: MemoryArticlePagingDataSource
) : PageNoKeyedPagingDataSource<List<Any>?>(MyApplication.PAGE_SIZE) {

    override fun getInitialPage(): Int {
        return 0
    }

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<Any>? {
        return MultiDataSourceHelper.successIfOneSuccess(
            requestType,
            { memoryBannerNotPagingDataSource.realLoadData(requestType) },
            { memoryTopArticleNotPagingDataSource.realLoadData(requestType) },
            pagingBlock = { memoryArticlePagingDataSource.realLoadData(requestType, pageNo, pageSize) }
        )
    }

}