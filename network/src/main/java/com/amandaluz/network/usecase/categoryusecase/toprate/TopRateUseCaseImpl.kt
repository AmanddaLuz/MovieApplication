package com.amandaluz.network.usecase.categoryusecase.toprate

import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.categoryrepository.toprate.TopRateRepository

class TopRateUseCaseImpl(private val repository: TopRateRepository): TopRateUseCase {
    override suspend fun getTopRate(apikey: String, page: Int): MovieResponse {
        val response = repository.getTopRate(apikey, language(), page)

        return when (response.code()) {
            200 -> response.body() ?: throw Exception()
            204 -> throw Exception("No_content")
            in 400..500 -> throw Exception("HttpError_topRateMovie")
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}