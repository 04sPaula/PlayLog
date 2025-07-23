package com.example.playlog

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,
    val nome: String,
    val descricao: String,
    val caminhoPoster: String?,
    val anoLancamento: Int,
    val generos: List<String>,
    val temporadaAtual: Int? = null,
    val episodioAtual: Int? = null,
    val lastWatchedTimestamp: Long? = null
)