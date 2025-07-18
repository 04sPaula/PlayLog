package com.example.playlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SeriesRepository

    private val _searchResults = MutableLiveData<List<SeriesEntity>>()
    val searchResults: LiveData<List<SeriesEntity>> = _searchResults

    init {
        val seriesDao = AppDatabase.getDatabase(application).seriesDao()
        repository = SeriesRepository(seriesDao)
    }

    fun searchSeries(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            val results = repository.searchSeriesOnApi(query)
            _searchResults.postValue(results) // postValue é seguro para usar em coroutines
        }
    }
}