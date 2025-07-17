package com.example.playlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val seriesDao: SeriesDao
    val allSeries: LiveData<List<SeriesEntity>>
    val lastWatchedSeries: LiveData<SeriesEntity?>

    init {
        val database = AppDatabase.getDatabase(application)
        seriesDao = database.seriesDao()
        allSeries = seriesDao.getAllSeries()
        lastWatchedSeries = seriesDao.getLastWatched()
    }

    fun insertTestData() {
        viewModelScope.launch {
            val testSeries = listOf(
                SeriesEntity(1, "Sex and the City", "A colunista Carrie Bradshaw e suas amigas...", null, 1998, listOf("Comédia", "Romance")),
                SeriesEntity(2, "Friends", "Acompanha a vida de seis amigos que vivem em Manhattan...", null, 1994, listOf("Comédia", "Sitcom")),
                SeriesEntity(3, "Breaking Bad", "Um professor de química do ensino médio...", null, 2008, listOf("Drama", "Crime", "Thriller")),
                SeriesEntity(4, "The Office", "Esta comédia em estilo de pseudodocumentário...", null, 2005, listOf("Comédia", "Sitcom")),
                SeriesEntity(5, "Grey's Anatomy", "A série acompanha a jornada de Meredith Grey...", null, 2005, listOf("Drama", "Médico"))
            )
            seriesDao.insertAll(testSeries)
        }
    }
}