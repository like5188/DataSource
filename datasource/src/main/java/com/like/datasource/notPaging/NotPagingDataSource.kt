package com.like.datasource.notPaging

import androidx.annotation.WorkerThread
import com.like.datasource.RequestType

/**
 * 不分页数据源。
 *
 * @param ResultType    返回的数据类型
 */
abstract class NotPagingDataSource<ResultType> : BaseNotPagingDataSource<ResultType>() {

    final override suspend fun realLoadData(requestType: RequestType): ResultType {
        return load(requestType)
    }

    /**
     * @param requestType   请求类型：[RequestType]
     */
    @WorkerThread
    protected abstract suspend fun load(requestType: RequestType): ResultType
}