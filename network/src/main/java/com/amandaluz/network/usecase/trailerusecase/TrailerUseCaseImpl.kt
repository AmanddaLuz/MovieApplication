package com.amandaluz.network.usecase.trailerusecase

import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import timber.log.Timber

class TrailerUseCaseImpl(
    private val repository: TrailerRepository
) : TrailerUseCase {

    override suspend fun getTrailerMovie(apikey: String, language: String, movieId: Int): List<ResultTrailer> {
        val response = repository.getTrailer(apikey, language, movieId)
        return when (response.code()) {
            200 -> response.body()?.results ?: throw Exception()
            else -> {
                Timber.tag(
                    "GET_TRAILER_RESPONSE:${response.code()} - ${response.errorBody()}"
                )
                throw IllegalArgumentException()
            }
        }
    }
}
