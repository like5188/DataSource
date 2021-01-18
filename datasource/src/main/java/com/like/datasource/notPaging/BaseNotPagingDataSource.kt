package com.like.datasource.notPaging

import androidx.annotation.WorkerThread
import com.like.datasource.DataSource
import com.like.datasource.RequestType
import com.like.datasource.Result

/**
 * 不分页数据源。
 *
 * @param ResultType    返回的数据类型
 */
abstract class BaseNotPagingDataSource<ResultType> : DataSource<ResultType>() {

    final override suspend fun loadData(requestType: RequestType): ResultType {
        return realLoadData(requestType)
    }

    final override fun result(): Result<ResultType> = Result(
        resultReportFlow = getResultReportFlow(),
        initial = this::initial,
        refresh = this::refresh,
        retry = this::retry
    )

    /**
     * @param requestType   请求类型：[RequestType]
     */
    @WorkerThread
    abstract suspend fun realLoadData(requestType: RequestType): ResultType
}