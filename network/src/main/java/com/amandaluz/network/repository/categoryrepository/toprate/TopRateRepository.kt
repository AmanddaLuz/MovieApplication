package com.amandaluz.network.repository.categoryrepository.toprate

import com.amandaluz.network.model.movie.MovieResponse
import retrofit2.Response

interface TopRateRepository {
    suspend fun getTopRate(apikey: String, language: String, page: Int): Response<MovieResponse>
}