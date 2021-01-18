package com.like.datasource.sample.paging.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
import com.like.common.util.datasource.collectWithProgress
import com.like.datasource.sample.R
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.databinding.ActivityPagingBinding
import com.like.datasource.sample.paging.dataSource.inDb.DbArticlePagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbPagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbTopArticleNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryArticlePagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryTopArticleNotPagingDataSource
import com.like.datasource.sample.paging.repository.PagingRepository
import com.like.datasource.sample.paging.viewModel.PagingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagingActivity : AppCompatActivity() {
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityPagingBinding>(this, R.layout.activity_paging)
    }
    private val mViewModel by lazy {
        ViewModelProvider(
            this, PagingViewModel.Factory(
                PagingRepository(
                    DbBannerNotPagingDataSource(this),
                    DbTopArticleNotPagingDataSource(this),
                    DbArticlePagingDataSource(this),
                    DbPagingDataSource(
                        DbBannerNotPagingDataSource(this),
                        DbTopArticleNotPagingDataSource(this),
                        DbArticlePagingDataSource(this)
                    ),
                    MemoryBannerNotPagingDataSource(),
                    MemoryTopArticleNotPagingDataSource(),
                    MemoryArticlePagingDataSource(),
                    MemoryPagingDataSource(
                        MemoryBannerNotPagingDataSource(),
                        MemoryTopArticleNotPagingDataSource(),
                        MemoryArticlePagingDataSource()
                    )
                )
            )
        ).get(PagingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding
        lifecycleScope.launch {
            mViewModel.getResult().collectWithProgress(
                this@PagingActivity,
                ProgressDialogFragment.newInstance(),
                { requestType, throwable ->
                    Logger.e("$throwable")
                },
                { requestType, result ->
                    Logger.printCollection(result, Log.INFO)
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

    fun clearDb(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            Db.getInstance(application).bannerEntityDao().deleteAll()
            Db.getInstance(application).topArticleEntityDao().deleteAll()
            Db.getInstance(application).articleEntityDao().deleteAll()
        }
    }

    fun queryDb(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            Db.getInstance(application).bannerEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
            Db.getInstance(application).topArticleEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
            Db.getInstance(application).articleEntityDao().getAll().forEach {
                Logger.i(it.toString())
            }
        }
    }
}
