package com.example.playlog

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val page: Int,
    val results: List<TvShowSearchResult>

)
