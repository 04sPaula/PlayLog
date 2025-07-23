package com.example.playlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application, seriesId: Int) : AndroidViewModel(application) {

    private val repository: SeriesRepository
    val seriesDetails: LiveData<SeriesEntity?>

    init {
        val seriesDao = AppDatabase.getDatabase(application).seriesDao()
        repository = SeriesRepository(seriesDao)
        seriesDetails = repository.getSeriesDetails(seriesId)
    }

    fun updateEpisodeProgress(season: Int, episode: Int) {
        viewModelScope.launch {
            val currentSeries = seriesDetails.value
            currentSeries?.let {
                val updatedSeries = it.copy(
                    temporadaAtual = season,
                    episodioAtual = episode,
                    lastWatchedTimestamp = System.currentTimeMillis()
                )
                repository.updateSeries(updatedSeries)
            }
        }
    }

    fun startWatching() {
        viewModelScope.launch {
            val currentSeries = seriesDetails.value
            currentSeries?.let {
                val updatedSeries = it.copy(lastWatchedTimestamp = System.currentTimeMillis())
                repository.updateSeries(updatedSeries)
            }
        }
    }

    fun deleteSeries() {
        viewModelScope.launch {
            val currentSeries = seriesDetails.value
            currentSeries?.let {
                repository.deleteSeries(it)
            }
        }
    }
}