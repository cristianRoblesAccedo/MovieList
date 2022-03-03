package com.example.movielist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movielist.models.TMDBService
import com.example.movielist.models.dataclasses.PopularMovieResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel(private val baseUrl: String, private val apiKey: String): ViewModel() {
    private val movieAPI = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val movieService = movieAPI.create(TMDBService::class.java)
    val movieLiveData = MutableLiveData<List<PopularMovieResponse>>()
    val movieSelectedLiveData = MutableLiveData<PopularMovieResponse>()
    val posterBaseUrlLiveData = MutableLiveData<String>()
    val logoBaseUrlLiveData = MutableLiveData<String>()

    init {
        // Updates list immediately after MovieViewModel gets instantiated
        updateMovieList()
        getImageInfo()
    }

    /**
     * Fetches the most recent list of popular movies from TMDB and posts the value on movieLiveData
     */
    fun updateMovieList() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = movieService.getPopularMovies(apiKey)
            val body = call.body()
            body?.let {
                movieLiveData.postValue(it.results)
            }
        }
    }

    /**
     * Gets poster and logo sizes so they can get successfully fetched from Glide
     */
    fun getImageInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = movieService.getConfiguration(apiKey)
            val body = call.body()
            // If petition has been done correctly
            body?.images?.let {
                posterBaseUrlLiveData.postValue(it.secureBaseUrl + it.posterSizes[it.posterSizes.size - 1])
                logoBaseUrlLiveData.postValue(it.secureBaseUrl + it.logoSizes[it.logoSizes.size - 1])
            }
        }
    }


    fun selectMovie(movie: PopularMovieResponse) {
        movieSelectedLiveData.postValue(movie)
    }
}