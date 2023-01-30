package com.amandaluz.network.usecase.categoryusecase.toprate

import com.amandaluz.network.model.movie.MovieResponse

interface TopRateUseCase {
    suspend fun getTopRate(apikey: String, page: Int): MovieResponse
}