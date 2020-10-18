package com.like.datasource.sample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.datasource.sample.data.model.ArticleEntity

@Dao
interface ArticleEntityDao : BaseDao<ArticleEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM ArticleEntity")
    fun deleteAll()

    @Query("SELECT * FROM ArticleEntity ORDER BY id ASC")
    fun getAll(): List<ArticleEntity>
}