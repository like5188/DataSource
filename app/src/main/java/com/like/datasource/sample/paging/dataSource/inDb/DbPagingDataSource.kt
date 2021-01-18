package com.like.datasource.sample.paging.dataSource.inDb

import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDbDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.sample.data.model.ArticleEntity
import com.like.datasource.sample.data.model.BannerInfo
import com.like.datasource.sample.data.model.TopArticleEntity
import com.like.datasource.util.MultiDataSourceHelper

class DbPagingDataSource(
    private val dbBannerNotPagingDataSource: DbBannerNotPagingDataSource,
    private val dbTopArticleNotPagingDataSource: DbTopArticleNotPagingDataSource,
    private val dbArticlePagingDataSource: DbArticlePagingDataSource,
) : PageNoKeyedPagingDataSource<List<Any>?>(MyApplication.PAGE_SIZE) {

    override fun getInitialPage(): Int {
        return 0
    }

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<Any>? {
        return MultiDataSourceHelper.successIfOneSuccess(
            requestType,
            { dbBannerNotPagingDataSource.realLoadData(requestType) },
            { dbTopArticleNotPagingDataSource.realLoadData(requestType) },
            pagingBlock = { dbArticlePagingDataSource.realLoadData(requestType, pageNo, pageSize) }
        )
    }

}