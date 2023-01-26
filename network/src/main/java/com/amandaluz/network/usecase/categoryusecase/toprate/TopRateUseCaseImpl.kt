package com.amandaluz.network.usecase.categoryusecase.toprate

import com.amandaluz.core.util.apikey
import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.categoryrepository.toprate.TopRateRepository
import timber.log.Timber

class TopRateUseCaseImpl(private val repository: TopRateRepository): TopRateUseCase {
    override suspend fun getTopRate(page: Int): MovieResponse? {
        val response = repository.getTopRate(apikey(), language(), page)

        return when (response.code()) {
            200 -> response.body()
            else -> {
                Timber.tag(
                    "GET_TOP_RATE_RESPONSE:${response.code()} - ${response.errorBody()}"
                )
                throw IllegalArgumentException()
            }
        }
    }
}