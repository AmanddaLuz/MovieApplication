package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.network.model.movie.Result

interface MovieUseCase {
    suspend fun getPopularMovie(apikey: String, language: String, page: Int): List<Result>

}