package com.example.playlog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    fun searchTvSeries(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.searchSeries(query = query)
                Log.d("API_SUCCESS", "Encontradas ${response.results.size} séries.")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Erro ao buscar séries: ${e.message}")
            }
        }
    }

    fun fetchEpisodes(seriesId: Int) {
        viewModelScope.launch {
            try {
                val seasonDetails = RetrofitClient.instance.getSeasonDetails(seriesId, 1)
                Log.d("API_SUCCESS", "Episódios da temporada 1: ${seasonDetails.episodes}")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Erro ao buscar episódios: ${e.message}")
            }
        }
    }
}