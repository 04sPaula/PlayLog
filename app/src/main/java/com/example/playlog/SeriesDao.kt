package com.example.playlog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SeriesDao {

    @Query("SELECT * FROM series ORDER BY nome ASC")
    fun getAllSeries(): LiveData<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE id = :seriesId")
    fun getSeriesById(seriesId: Int): LiveData<SeriesEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(series: List<SeriesEntity>)

    @Query("SELECT * FROM series WHERE lastWatchedTimestamp IS NOT NULL ORDER BY lastWatchedTimestamp DESC LIMIT 1")
    fun getLastWatched(): LiveData<SeriesEntity?>

    @Update
    suspend fun updateSeries(series: SeriesEntity)

    @Query("SELECT * FROM series WHERE nome LIKE '%' || :query || '%'")
    fun searchSeries(query: String): LiveData<List<SeriesEntity>>
}