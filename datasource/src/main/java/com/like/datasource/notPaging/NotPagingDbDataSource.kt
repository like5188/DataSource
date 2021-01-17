package com.like.datasource.notPaging

import android.util.Log
import androidx.annotation.WorkerThread
import com.like.datasource.DataSource
import com.like.datasource.RequestType
import com.like.datasource.Result

/**
 * 不分页数据源。带数据库操作。
 *
 * @param ResultType    返回的数据类型
 */
abstract class NotPagingDbDataSource<ResultType> : DataSource<ResultType>() {

    final override suspend fun loadData(requestType: RequestType): ResultType {
        var data = loadFromDb(requestType)
        if (shouldFetch(requestType, data)) {
            Log.d("NotPagingDbDataSource", "即将从网络获取数据并存入数据库中")
            fetchFromNetworkAndSaveToDb(requestType)
            Log.d("NotPagingDbDataSource", "即将重新从数据库获取数据")
            data = loadFromDb(requestType)
        }
        Log.d("NotPagingDbDataSource", "从数据库获取到了数据：$data")
        return data
    }

    final override fun result(): Result<ResultType> = Result(
        resultReportFlow = getResultReportFlow(),
        initial = this::initial,
        refresh = this::refresh,
        retry = this::retry
    )

    /**
     * 从数据库中获取数据
     */
    @WorkerThread
    protected abstract suspend fun loadFromDb(requestType: RequestType): ResultType

    /**
     * 是否应该从网络获取数据。
     */
    @WorkerThread
    protected abstract fun shouldFetch(requestType: RequestType, resultType: ResultType): Boolean

    /**
     * 从网络获取到数据并存入数据库中。
     */
    @WorkerThread
    protected abstract suspend fun fetchFromNetworkAndSaveToDb(requestType: RequestType)

}