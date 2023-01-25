package com.amandaluz.network.repository.movierepository

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import retrofit2.Response

class MovieRepositoryImpl(private val api: MovieApi): MovieRepository {
    override suspend fun getMovie(
        apikey: String,
        language: String,
        page: Int
    ): Response<MovieResponse> =
        api.getPopularMovies(apikey, language, page)
}