package com.amandaluz.network.usecase.categoryusecase.upcoming

import com.amandaluz.core.util.apikey
import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.categoryrepository.upcoming.UpcomingRepository
import timber.log.Timber

class UpcomingUseCaseImpl(private val repository: UpcomingRepository): UpcomingUseCase {
    override suspend fun getUpcoming(page: Int): MovieResponse? {
        val response = repository.getUpcoming(apikey(), language(), page)

        return when (response.code()) {
            200 -> response.body()
            else -> {
                Timber.tag(
                    "GET_UPCOMING_RESPONSE:${response.code()} - ${response.errorBody()}"
                )
                throw IllegalArgumentException()
            }
        }
    }
}