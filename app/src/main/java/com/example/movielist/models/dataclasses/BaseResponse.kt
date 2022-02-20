package com.example.movielist.models.dataclasses

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("page") val page: String,
    @SerializedName("results") val results: List<PopularMovieResponse>
)
