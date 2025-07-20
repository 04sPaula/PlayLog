package com.example.playlog

fun TvShowDetails.toSeriesEntity(): SeriesEntity {

    fun extrairAno(data: String?): Int {
        return data?.take(4)?.toIntOrNull() ?: 0
    }

    return SeriesEntity(
        id = this.id,
        nome = this.name,
        descricao = this.overview ?: "",
        caminhoPoster = if (this.posterPath != null) "https://image.tmdb.org/t/p/w500${this.posterPath}" else null,
        anoLancamento = extrairAno(this.firstAirDate),
        generos = this.genres?.map { it.name } ?: emptyList(),
        lastWatchedTimestamp = null
    )
}

fun TvShowSearchResult.toSeriesEntity(): SeriesEntity {
    fun extrairAno(data: String?): Int {
        return data?.take(4)?.toIntOrNull() ?: 0
    }

    return SeriesEntity(
        id = this.id,
        nome = this.name,
        descricao = this.overview,
        caminhoPoster = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        anoLancamento = extrairAno(this.firstAirDate),
        generos = emptyList(),
        lastWatchedTimestamp = null
    )
}