package com.example.playlog

fun TvShow.toSeriesEntity(): SeriesEntity {
    fun extrairAno(data: String?): Int {
        return data?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    return SeriesEntity(
        id = this.id,
        nome = this.name,
        descricao = this.overview,
        caminhoPoster = this.posterPath,
        anoLancamento = extrairAno(this.firstAirDate),
        generos = this.genreIds.map { it.toString() }
    )
}