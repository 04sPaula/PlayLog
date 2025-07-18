package com.example.playlog

import android.util.Log

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

}