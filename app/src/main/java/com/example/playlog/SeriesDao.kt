package com.example.playlog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SeriesDao {

    @Query("SELECT * FROM series ORDER BY nome ASC")
    fun getAllSeries(): LiveData<List<SeriesEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(series: List<SeriesEntity>)
}