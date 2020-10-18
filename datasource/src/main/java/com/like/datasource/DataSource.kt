package com.like.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 数据源基类
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class DataSource<ResultType> {
    private val isRunning = AtomicBoolean(false)
    private var mFailureRequestType: RequestType? = null
    private val _controlCh = ConflatedBroadcastChannel<RequestType>()

    protected fun getResultReportFlow(): Flow<ResultReport<ResultType>> = channelFlow {
        _controlCh.asFlow().collect { requestType ->
            send(ResultReport(requestType, RequestState.Running))
            mFailureRequestType = try {
                val data = withContext(Dispatchers.IO) {
                    loadData(requestType)
                }
                send(ResultReport(requestType, RequestState.Success(data)))
                null
            } catch (e: Exception) {
                send(ResultReport(requestType, RequestState.Failed(e)))
                requestType
            } finally {
                isRunning.compareAndSet(true, false)
            }
        }
    }

    protected fun initial() {
        load(RequestType.Initial)
    }

    protected fun refresh() {
        load(RequestType.Refresh)
    }

    protected fun loadAfter() {
        load(RequestType.After)
    }

    protected fun loadBefore() {
        load(RequestType.Before)
    }

    protected fun retry() {
        mFailureRequestType?.let {
            load(it)
        }
    }

    private fun load(requestType: RequestType) {
        if (isRunning.compareAndSet(false, true)) {
            _controlCh.offer(requestType)
        }
    }

    protected abstract suspend fun loadData(requestType: RequestType): ResultType

    abstract fun result(): Result<ResultType>

}