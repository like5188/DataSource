package com.like.datasource.util

import com.like.datasource.RequestState
import com.like.datasource.RequestType
import com.like.datasource.Result
import com.like.datasource.ResultReport
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

//[com.like.repository.Result]相关的基础绑定操作
/**
 * 绑定成功失败回调。
 *
 * @param onSuccess         成功回调
 * @param onFailed          失败回调
 */
suspend fun <ResultType> Result<ResultType>.collect(
    onFailed: ((ResultReport<Nothing>) -> Unit)? = null,
    onSuccess: ((ResultType) -> Unit)? = null
) {
    resultReportFlow.collect { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        when (state) {
            is RequestState.Failed -> {
                onFailed?.invoke(resultReport as ResultReport<Nothing>)
            }
            is RequestState.Success<ResultType> -> {
                onSuccess?.invoke(state.data)
            }
        }
    }
}

/**
 * 初始化或者刷新时控制进度条的显示隐藏。
 *
 * @param show      初始化或者刷新开始时显示进度条
 * @param hide      初始化或者刷新成功或者失败时隐藏进度条
 */
fun <ResultType> Result<ResultType>.progress(
    show: () -> Unit,
    hide: () -> Unit
): Result<ResultType> {
    val newResultReportFlow = resultReportFlow.onEach { resultReport ->
        val state = resultReport.state
        val type = resultReport.type
        if (type is RequestType.Initial || type is RequestType.Refresh) {
            when (state) {
                is RequestState.Running -> {
                    show()
                }
                else -> {
                    hide()
                }
            }
        }
    }
    return update(newResultReportFlow)
}