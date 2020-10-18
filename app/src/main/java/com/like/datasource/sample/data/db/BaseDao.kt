package com.like.datasource.sample.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    fun insert(vararg objects: T)

    @Update
    fun update(vararg objects: T)

    @Delete
    fun delete(vararg objects: T)
}