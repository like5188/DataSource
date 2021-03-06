package com.like.datasource.sample.paging.repository

import com.like.datasource.sample.paging.dataSource.inDb.DbArticlePagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbPagingDataSource
import com.like.datasource.sample.paging.dataSource.inDb.DbTopArticleNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryArticlePagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryBannerNotPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryPagingDataSource
import com.like.datasource.sample.paging.dataSource.inMemory.MemoryTopArticleNotPagingDataSource

class PagingRepository(
    private val dbBannerNotPagingDataSource: DbBannerNotPagingDataSource,
    private val dbTopArticleNotPagingDataSource: DbTopArticleNotPagingDataSource,
    private val dbArticlePagingDataSource: DbArticlePagingDataSource,
    private val dbPagingDataSource: DbPagingDataSource,
    private val memoryBannerNotPagingDataSource: MemoryBannerNotPagingDataSource,
    private val memoryTopArticleNotPagingDataSource: MemoryTopArticleNotPagingDataSource,
    private val memoryArticlePagingDataSource: MemoryArticlePagingDataSource,
    private val memoryPagingDataSource: MemoryPagingDataSource
) {
    fun getResult() = dbPagingDataSource.result()
}