package com.amandaluz.network.repository.categoryrepository.toprate

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import retrofit2.Response

class TopRateRepositoryImpl(private val api: MovieApi): TopRateRepository {
    override suspend fun getTopRate(
        apikey: String,
        language: String,
        page: Int
    ): Response<MovieResponse> = api.getTopRatedMovies(apikey, language, page)
}