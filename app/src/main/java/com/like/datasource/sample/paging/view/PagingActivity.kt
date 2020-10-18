package com.like.datasource.sample.paging.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
import com.like.datasource.sample.R
import com.like.datasource.sample.databinding.ActivityPagingBinding
import com.like.datasource.sample.paging.dataSource.inDb.DbBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbTopArticleNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryArticlePagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryTopArticleNotPagingDataSource
import com.like.datasource.sample.paging.repository.PagingRepository
import com.like.datasource.sample.paging.viewModel.PagingViewModel
import com.like.datasource.util.collect
import com.like.datasource.util.progress
import kotlinx.coroutines.launch

class PagingActivity : AppCompatActivity() {
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityPagingBinding>(this, R.layout.activity_paging)
    }
    private val mViewModel by lazy {
        ViewModelProvider(
            this, PagingViewModel.Factory(
                PagingRepository(
                    MemoryBannerNotPagingDataSource(),
                    MemoryTopArticleNotPagingDataSource(),
                    MemoryArticlePagingDataSource(),
                    DbBannerNotPagingDataSource(this),
                    DbTopArticleNotPagingDataSource(this),
                    MemoryDataSource()
                )
            )
        ).get(PagingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding
        lifecycleScope.launch {
            mViewModel.getResult().progress(
                {
                    Logger.v("show")
                },
                {
                    Logger.d("hide")
                }
            ).collect(
                {
                    Logger.e("$it")
                },
                {
                    Logger.printCollection(it, Log.INFO)
                }
            )
        }
    }

    fun initial(view: View) {
        mViewModel.getResult().initial()
    }

    fun refresh(view: View) {
        mViewModel.getResult().refresh()
    }

    fun loadAfter(view: View) {
        mViewModel.getResult().loadAfter?.invoke()
    }

    fun loadBefore(view: View) {
        mViewModel.getResult().loadBefore?.invoke()
    }

    fun retry(view: View) {
        mViewModel.getResult().retry()
    }
}
