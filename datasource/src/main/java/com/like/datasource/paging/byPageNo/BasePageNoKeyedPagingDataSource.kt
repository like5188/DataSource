package com.like.datasource.paging.byPageNo

import androidx.annotation.WorkerThread
import com.like.datasource.DataSource
import com.like.datasource.RequestType
import com.like.datasource.Result

/**
 * 根据自己维护的 pageNo 来作为分页 key 的分页数据源。
 *
 * @param ResultType    返回的数据类型
 * @param isLoadAfter   true：往后加载更多（默认值）；false：往前加载更多。
 */
abstract class BasePageNoKeyedPagingDataSource<ResultType>(
    private val pageSize: Int,
    private val isLoadAfter: Boolean = true
) : DataSource<ResultType>() {
    private var pageNo: Int = 1

    final override suspend fun loadData(requestType: RequestType): ResultType {
        when (requestType) {
            is RequestType.Initial, is RequestType.Refresh -> {
                pageNo = getInitialPage()
            }
            is RequestType.Before -> {
                pageNo--
            }
            is RequestType.After -> {
                pageNo++
            }
        }
        return try {
            realLoadData(requestType, pageNo, pageSize)
        } catch (e: Exception) {
            // 还原 pageNo
            when (requestType) {
                is RequestType.Before -> {
                    pageNo++
                }
                is RequestType.After -> {
                    pageNo--
                }
            }
            throw e
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
     * 获取初始页码。默认为1
     */
    protected open fun getInitialPage(): Int = 1

    /**
     * @param requestType   请求类型：[RequestType]
     * @param pageNo        页码
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun realLoadData(requestType: RequestType, pageNo: Int, pageSize: Int): ResultType

}