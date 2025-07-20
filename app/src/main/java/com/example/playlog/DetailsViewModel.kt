package com.example.playlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application, seriesId: Int) : AndroidViewModel(application) {

    private val repository: SeriesRepository
    val seriesDao = AppDatabase.getDatabase(application).seriesDao()
    val seriesDetails: LiveData<SeriesEntity?>

    init {
        repository = SeriesRepository(seriesDao)
        seriesDetails = repository.getSeriesDetails(seriesId)
    }

    fun markAsWatched() {
        viewModelScope.launch {
            val series = seriesDetails.value
            series?.let {
                val updatedSeries = it.copy(lastWatchedTimestamp = System.currentTimeMillis())
                seriesDao.updateSeries(updatedSeries)
            }
        }
    }
}