package com.amandaluz.network.repository.movierepository

import com.amandaluz.network.model.movie.MovieResponse
import retrofit2.Response

interface MovieRepository {
    suspend fun getMovie(apikey: String, language: String, page: Int): Response<MovieResponse>
}