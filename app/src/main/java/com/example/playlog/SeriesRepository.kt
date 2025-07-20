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

    fun getSeriesDetails(seriesId: Int): LiveData<SeriesEntity?> {
        return liveData(Dispatchers.IO) {
            var series = seriesDao.getSeriesByIdSync(seriesId)

            if (series != null) {
                emit(series)
            } else {
                // 3. Se NÃO foi encontrada, busca na API.
                try {
                    val seriesFromApi = tmdbService.getSeriesDetails(seriesId)
                    val seriesEntity = seriesFromApi.toSeriesEntity()
                    // Emite o resultado da API
                    emit(seriesEntity)
                } catch (e: Exception) {
                    Log.e("DetailsRepository", "Erro ao buscar ou converter detalhes da API", e)
                    // Se a API falhar, emite nulo
                    emit(null)
                }
            }
        }
    }
}