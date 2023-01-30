package com.amandaluz.network.usecase.categoryusecase.upcoming

import com.amandaluz.network.model.movie.MovieResponse

interface UpcomingUseCase {
    suspend fun getUpcoming(apikey: String, page: Int): MovieResponse
}