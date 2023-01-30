package com.amandaluz.network.usecase.trailerusecase

import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.repository.trailerrepository.TrailerRepository

class TrailerUseCaseImpl(
    private val repository: TrailerRepository
) : TrailerUseCase {

    override suspend fun getTrailerMovie(apikey: String, language: String, movieId: Int): List<ResultTrailer> {
        val response = repository.getTrailer(apikey, language, movieId)

        return when (response.code()) {
            200 -> response.body()?.results ?: throw Exception()
            204 -> throw Exception("No_content")
            in 400..500 -> throw Exception("HttpError")
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}
