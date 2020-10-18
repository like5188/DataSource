package com.like.datasource.sample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.datasource.sample.data.model.BannerInfo

@Dao
interface BannerEntityDao : BaseDao<BannerInfo.BannerEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<BannerInfo.BannerEntity>)

    @Query("DELETE FROM BannerEntity")
    fun deleteAll()

    @Query("SELECT * FROM BannerEntity ORDER BY id ASC")
    fun getAll(): List<BannerInfo.BannerEntity>
}