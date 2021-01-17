package com.like.datasource.paging.byData

import androidx.annotation.WorkerThread
import com.like.datasource.DataSource
import com.like.datasource.RequestType
import com.like.datasource.Result

/**
 * 根据接口返回的数据，由用户来确定分页 key 的分页数据源。
 *
 * @param Key           分页标记数据类型
 * @param ResultType    返回的数据类型
 * @param isLoadAfter   true：往后加载更多（默认值）；false：往前加载更多。
 */
abstract class BaseDataKeyedPagingDataSource<Key : Any, ResultType>(
    private val pageSize: Int,
    private val isLoadAfter: Boolean = true
) : DataSource<ResultType>() {
    private var key: Key? = null

    final override suspend fun loadData(requestType: RequestType): ResultType {
        if (requestType is RequestType.Initial || requestType is RequestType.Refresh) {
            key = null
        }
        return realLoadData(requestType, key, pageSize).apply {
            key = getKey(this)
        }
    }

    final override fun result(): Result<ResultType> = Result(
        resultReportFlow = getResultReportFlow(),
        initial = this::initial,
        refresh = this::refresh,
        retry = this::retry,
        loadAfter = if (isLoadAfter) this::loadAfter else null,
        loadBefore = if (isLoadAfter) null else this::loadBefore
    )

    /**
     * 根据返回的数据获取上一页或者下一页的 key。
     */
    protected abstract fun getKey(data: ResultType): Key?

    /**
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun realLoadData(requestType: RequestType, key: Key?, pageSize: Int): ResultType

}