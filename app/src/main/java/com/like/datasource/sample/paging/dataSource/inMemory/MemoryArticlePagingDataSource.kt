package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.sample.data.model.ArticleEntity
import com.like.datasource.sample.data.netWork.RetrofitUtils
import kotlinx.coroutines.delay

class MemoryArticlePagingDataSource : PageNoKeyedPagingDataSource<List<ArticleEntity>?>(MyApplication.PAGE_SIZE) {
    private var i = 0
    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<ArticleEntity>? {
        delay(1000)
        if (i++ < 1) {
            throw RuntimeException("模拟错误")
        }
        return RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas
    }

    override fun getInitialPage(): Int {
        return 0
    }

}