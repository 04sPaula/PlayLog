package com.example.playlog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,
    val nome: String,
    val descricao: String,
    val caminhoPoster: String?,
    val anoLancamento: Int,
    val generos: List<String>,
    val lastWatchedTimestamp: Long? = null
)