package com.example.playlog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int, // O ID do TMDb, para nunca ter duplicatas
    val nome: String,
    val descricao: String, // Usando String, que no Room vira TEXT (sem limite prático)
    val caminhoPoster: String?, // O "poster_path" da API
    val anoLancamento: Int,
    val generos: List<String> // O Room pode converter listas de tipos simples
)