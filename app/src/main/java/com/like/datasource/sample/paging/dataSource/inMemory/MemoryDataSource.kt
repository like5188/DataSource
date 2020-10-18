package com.like.datasource.sample.paging.dataSource.inMemory

import com.like.datasource.RequestType
import com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource
import com.like.datasource.sample.MyApplication
import com.like.datasource.sample.data.netWork.RetrofitUtils
import com.like.datasource.util.MultiDataSourceHelper
import kotlinx.coroutines.delay

class MemoryDataSource : PageNoKeyedPagingDataSource<List<Any>?>(MyApplication.PAGE_SIZE) {

    override suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): List<Any>? {
        delay(1000)
        return MultiDataSourceHelper.successIfOneSuccess(
            requestType,
            { RetrofitUtils.retrofitApi.getBanner().getDataIfSuccess() },
            { RetrofitUtils.retrofitApi.getTopArticle().getDataIfSuccess() },
            pagingBlock = { RetrofitUtils.retrofitApi.getArticle(pageNo).getDataIfSuccess()?.datas }
        )
    }

    override fun getInitialPage(): Int {
        return 0
    }

}