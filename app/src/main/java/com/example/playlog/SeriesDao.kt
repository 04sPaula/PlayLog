package com.example.playlog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SeriesDao {

    @Query("SELECT * FROM series ORDER BY nome ASC")
    fun getAllSeries(): LiveData<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE id = :seriesId")
    fun getSeriesById(seriesId: Int): LiveData<SeriesEntity?>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(series: List<SeriesEntity>)

    @Query("SELECT * FROM series WHERE lastWatchedTimestamp IS NOT NULL ORDER BY lastWatchedTimestamp DESC LIMIT 1")
    fun getLastWatched(): LiveData<SeriesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSeries(series: SeriesEntity)

    @Query("SELECT * FROM series WHERE nome LIKE '%' || :query || '%'")
    fun searchSeries(query: String): LiveData<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE id = :seriesId")
    fun getSeriesByIdSync(seriesId: Int): SeriesEntity?

    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    @Query("DELETE FROM series")
    suspend fun deleteAllSeries()

    @Query("SELECT * FROM series")
    suspend fun getAllSeriesSync(): List<SeriesEntity>
}