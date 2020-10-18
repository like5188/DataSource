package com.like.datasource.util

import com.like.datasource.RequestType
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch

object MultiDataSourceHelper {

    /**
     * 合并请求：
     * ①、全部为 Success，则返回 Success。
     * ②、只要有一个 Error，则返回 Error。
     */
    suspend fun <ResultType> successIfAllSuccess(
        requestType: RequestType,
        vararg notPagingBlocks: suspend () -> List<ResultType>?,
        pagingBlock: (suspend () -> List<ResultType>?)? = null
    ): List<ResultType> = coroutineScope {
        val totalBlocksNumber = checkSize(notPagingBlocks = notPagingBlocks, pagingBlock = pagingBlock)

        val notPagingBlocksDeferredArray = getNotPagingBlocksDeferredArray(this, requestType, notPagingBlocks = notPagingBlocks)
        val pagingResult = getPagingBlockDeferred(this, pagingBlock)

        val result = mutableListOf<ResultType>()
        suspend fun addResultOrThrow(deferred: Deferred<List<ResultType>?>?) {
            deferred?.await()?.let {
                result.addAll(it)
            }
        }
        notPagingBlocksDeferredArray.forEach { deferred ->
            addResultOrThrow(deferred)
        }
        addResultOrThrow(pagingResult)

        result
    }

    /**
     * 合并请求：
     * ①、只要有一个 Success，则返回 Success。
     * ②、全部为 Error，则返回 Error。
     */
    suspend fun <ResultType> successIfOneSuccess(
        requestType: RequestType,
        vararg notPagingBlocks: suspend () -> List<ResultType>?,
        pagingBlock: (suspend () -> List<ResultType>?)? = null
    ): List<ResultType> = supervisorScope {
        val totalBlocksNumber = checkSize(notPagingBlocks = notPagingBlocks, pagingBlock = pagingBlock)

        val notPagingBlocksDeferredArray = getNotPagingBlocksDeferredArray(this, requestType, notPagingBlocks = notPagingBlocks)
        val pagingResult = getPagingBlockDeferred(this, pagingBlock)

        val result = mutableListOf<ResultType>()
        val totalExceptionTimes = when {
            requestType is RequestType.Initial || requestType is RequestType.Refresh -> CountDownLatch(totalBlocksNumber)
            pagingBlock != null -> CountDownLatch(1)
            else -> CountDownLatch(0)
        }

        var firstException: Throwable? = null

        suspend fun addResult(deferred: Deferred<List<ResultType>?>?) = try {
            deferred?.await()?.let {
                result.addAll(it)
            }
        } catch (e: Exception) {
            if (firstException == null) {
                firstException = e
            }
            totalExceptionTimes.countDown()
        }

        notPagingBlocksDeferredArray.forEach { deferred ->
            addResult(deferred)
        }
        addResult(pagingResult)
        if (totalExceptionTimes.count == 0L) {//全部失败
            throw firstException ?: IllegalArgumentException("pagingBlock is null")
        }

        result
    }

    private fun <ResultType> checkSize(
        vararg notPagingBlocks: suspend () -> List<ResultType>?,
        pagingBlock: (suspend () -> List<ResultType>?)? = null
    ): Int {
        val totalBlocksNumber = notPagingBlocks.size + if (pagingBlock == null) 0 else 1
        if (totalBlocksNumber < 2) {
            throw IllegalArgumentException("totalBlocksNumber less than 2")
        }
        return totalBlocksNumber
    }

    private fun <ResultType> getPagingBlockDeferred(
        coroutineScope: CoroutineScope,
        pagingBlock: (suspend () -> List<ResultType>?)?
    ) = if (pagingBlock != null) {
        coroutineScope.async(Dispatchers.IO) {
            pagingBlock()
        }
    } else {
        null
    }

    private fun <ResultType> getNotPagingBlocksDeferredArray(
        coroutineScope: CoroutineScope,
        requestType: RequestType,
        vararg notPagingBlocks: suspend () -> List<ResultType>?
    ) = arrayOfNulls<Deferred<List<ResultType>?>?>(notPagingBlocks.size).apply {
        if (requestType is RequestType.Initial || requestType is RequestType.Refresh) {
            notPagingBlocks.forEachIndexed { index, suspendFunction0 ->
                this[index] = coroutineScope.async(Dispatchers.IO) {
                    suspendFunction0()
                }
            }
        }
    }
}