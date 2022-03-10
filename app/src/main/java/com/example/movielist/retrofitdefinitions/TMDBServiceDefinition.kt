package com.example.movielist.retrofitdefinitions

import com.example.movielist.models.BaseResponse
import com.example.movielist.models.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBServiceDefinition {
    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page: Int = 1): Response<BaseResponse>

    @GET("/3/configuration")
    suspend fun getConfiguration(@Query("api_key") apiKey: String): Response<BaseResponse>

    @GET("/3/genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String): Response<GenreResponse>
}