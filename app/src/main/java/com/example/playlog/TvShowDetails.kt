package com.example.playlog

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvShowDetails(
    val id: Int,
    val name: String,

    @Json(name = "overview")
    val overview: String?,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "first_air_date")
    val firstAirDate: String?,

    val genres: List<Genre>?
    )
