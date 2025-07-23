package com.example.playlog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class SeriesRepository(private val seriesDao: SeriesDao) {

    private val tmdbService = RetrofitClient.instance

    suspend fun searchSeriesOnApi(query: String): List<SeriesEntity> {
        return try {
            val response = tmdbService.searchSeries(query = query)
            response.results.map { it.toSeriesEntity() }
        } catch (e: Exception) {
            Log.e("Repository", "Erro na busca", e)
            emptyList()
        }
    }

    fun getAllSeries(): LiveData<List<SeriesEntity>> {
        return seriesDao.getAllSeries()
    }

    fun getSeriesDetails(seriesId: Int): LiveData<SeriesEntity?> {
        return liveData(Dispatchers.IO) {
            var series = seriesDao.getSeriesByIdSync(seriesId)

            if (series != null) {
                emit(series)
            } else {
                try {
                    val seriesFromApi = tmdbService.getSeriesDetails(seriesId)
                    val seriesEntity = seriesFromApi.toSeriesEntity()
                    emit(seriesEntity)
                } catch (e: Exception) {
                    Log.e("DetailsRepository", "Erro ao buscar ou converter detalhes da API", e)
                    emit(null)
                }
            }
        }
    }

    suspend fun updateSeries(series: SeriesEntity) {
        seriesDao.upsertSeries(series)
    }

    suspend fun deleteSeries(series: SeriesEntity) {
        seriesDao.deleteSeries(series)
    }
}