package com.example.playlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class AllWatchedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SeriesRepository
    val allWatchedSeries: LiveData<List<SeriesEntity>>

    init {
        val seriesDao = AppDatabase.getDatabase(application).seriesDao()
        repository = SeriesRepository(seriesDao)
        allWatchedSeries = repository.getAllSeries()
    }
}