package com.example.playlog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progresso_usuario",
    foreignKeys = [
        ForeignKey(
            entity = SeriesEntity::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ProgressoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val seriesId: Int,
    var status: StatusAssistido,
    var temporadaAtual: Int,
    var episodioAtual: Int,
    var ultimaVezAssistido: Long,
    var nota: Float?
)

enum class StatusAssistido {
    ASSISTINDO,
    FINALIZADO,
    PARA_ASSISTIR,
    ABANDONADO
}