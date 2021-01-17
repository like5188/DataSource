package com.like.datasource.paging.byPageNo

import androidx.annotation.WorkerThread
import com.like.datasource.RequestType

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 *
 * @param ResultType    返回的数据类型
 * @param isLoadAfter   true：往后加载更多（默认值）；false：往前加载更多。
 */
abstract class PageNoKeyedPagingDataSource<ResultType>(pageSize: Int, isLoadAfter: Boolean = true) :
    BasePageNoKeyedPagingDataSource<ResultType>(pageSize, isLoadAfter) {

    final override suspend fun realLoadData(requestType: RequestType, pageNo: Int, pageSize: Int): ResultType {
        return load(requestType, pageNo, pageSize)
    }

    /**
     * @param requestType   请求类型：[RequestType]
     * @param pageNo        页码
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun load(requestType: RequestType, pageNo: Int, pageSize: Int): ResultType

}