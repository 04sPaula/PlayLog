package com.example.playlog

import com.squareup.moshi.Json

data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "first_air_date")
    val firstAirDate: String?,

    @Json(name = "genre_ids")
    val genreIds: List<Int>
    )
