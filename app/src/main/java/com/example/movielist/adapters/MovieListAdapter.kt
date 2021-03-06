package com.example.movielist.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movielist.R
import com.example.movielist.models.GenreGroup
import com.example.movielist.models.PopularMovieResponse
import com.example.movielist.retrofitservices.TMDBService
import com.example.movielist.viewmodel.MovieViewModel
import com.google.firebase.analytics.FirebaseAnalytics

class MovieListAdapter(
    private val viewModel: MovieViewModel,
    private val movieService: TMDBService,
    private val context: Context,
    private val genre: Int): RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private var movies = mutableListOf<PopularMovieResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_card, parent, false)
        return ViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (movies.size > 0)
            holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateList(newMovieMap: Map<Int, GenreGroup>) {
        newMovieMap[genre]?.movies?.let {
            movies = it
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val view: View, private val viewModel: MovieViewModel): RecyclerView.ViewHolder(view) {
        private val ivMovieImage = view.findViewById<ImageView>(R.id.iv_movie_image)
        private val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        private val tvVoteAverage = view.findViewById<TextView>(R.id.tv_rating)
        private val tvDate = view.findViewById<TextView>(R.id.tv_date)

        fun bind(movie: PopularMovieResponse) {
            tvTitle.text = movie.title
            tvVoteAverage.text = movie.voteAverage.toString()
            tvDate.text = movie.date

            // Renders image on card
            viewModel.configurationLiveData.value?.let {
                Glide.with(view)
                    .load(view.context.getString(R.string.image_base_url) + it.logoSizes[it.logoSizes.size - 1] + movie.poster)
                    .fitCenter()
                    .into(ivMovieImage)
            }

            view.setOnClickListener {
                // Gets Google Analytics instance
                val firebase = FirebaseAnalytics.getInstance(view.context)
                val bundle = Bundle()

                // Gets movie trailer
                movieService.getMovieVideos(
                    movie.id,
                    context.getString(R.string.default_video_site),
                    context.getString(R.string.default_video_type))

                // Logs the click event
                bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, movie.id)
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movie.title)
                firebase.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)

                // Selects movie info to be displayed on MovieInfo view via ViewModel
                viewModel.selectMovie(movie)
                // Navigates to MovieInfo fragment
                view.findNavController().navigate(R.id.action_mainPager_to_movieInfo)
            }
        }
    }
}