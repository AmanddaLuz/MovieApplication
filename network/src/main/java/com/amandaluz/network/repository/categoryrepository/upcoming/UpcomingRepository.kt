package com.amandaluz.network.repository.categoryrepository.upcoming

import com.amandaluz.network.model.movie.MovieResponse
import retrofit2.Response

interface UpcomingRepository {
    suspend fun getUpcoming(apikey: String, language: String, page: Int): Response<MovieResponse>
}