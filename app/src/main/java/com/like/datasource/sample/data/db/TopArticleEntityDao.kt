package com.like.datasource.sample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.datasource.sample.data.model.TopArticleEntity

@Dao
interface TopArticleEntityDao : BaseDao<TopArticleEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<TopArticleEntity>)

    @Query("DELETE FROM TopArticleEntity")
    fun deleteAll()

    @Query("SELECT * FROM TopArticleEntity ORDER BY id ASC")
    fun getAll(): List<TopArticleEntity>
}