package com.amandaluz.network.repository.categoryrepository.upcoming

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import retrofit2.Response

class UpcomingRepositoryImpl(private val api: MovieApi): UpcomingRepository {
    override suspend fun getUpcoming(
        apikey: String,
        language: String,
        page: Int
    ): Response<MovieResponse> = api.getUpComingMovies(apikey, language, page)
}