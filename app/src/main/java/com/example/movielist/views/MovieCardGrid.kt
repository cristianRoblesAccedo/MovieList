package com.example.movielist.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.movielist.R
import com.example.movielist.databinding.FragmentMovieCardGridBinding

class MovieCardGrid : Fragment() {
    private lateinit var binding: FragmentMovieCardGridBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_card_grid, container, false)
        return binding.root
    }
}