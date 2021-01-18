package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.sample.data.model.ArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils

class MemoryArticlePagingDataSource : PageNoKeyedPagingDataSource<List<ArticleEntity>?>(MyApplication.PAGE_SIZE) {

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<ArticleEntity>? {
        return RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
    }

    override fun getInitialPage(): Int {
        return 0
    }

}