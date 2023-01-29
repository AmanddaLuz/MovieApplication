package com.amandaluz.network.usecase.trailerusecase

import com.amandaluz.network.model.trailer.ResultTrailer

interface TrailerUseCase {
    suspend fun getTrailerMovie(apikey: String, language: String, movieId: Int): List<ResultTrailer>
}