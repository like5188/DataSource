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
abstract class DataKeyedPagingDataSource<Key : Any, ResultType>(pageSize: Int, isLoadAfter: Boolean = true) :
    BaseDataKeyedPagingDataSource<Key, ResultType>(pageSize, isLoadAfter) {

    final override suspend fun realLoadData(requestType: RequestType, key: Key?, pageSize: Int): ResultType {
        return load(requestType, key, pageSize)
    }

    /**
     * @param requestType   请求类型：[RequestType]
     * @param key           上一页、下一页的标记。
     * @param pageSize      每页加载数量
     */
    @WorkerThread
    protected abstract suspend fun load(requestType: RequestType, key: Key?, pageSize: Int): ResultType

}