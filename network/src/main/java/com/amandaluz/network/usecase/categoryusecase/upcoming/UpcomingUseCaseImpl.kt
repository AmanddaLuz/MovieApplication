package com.amandaluz.network.usecase.categoryusecase.upcoming

import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.categoryrepository.upcoming.UpcomingRepository

class UpcomingUseCaseImpl(private val repository: UpcomingRepository): UpcomingUseCase {
    override suspend fun getUpcoming(apikey: String, page: Int): MovieResponse {
        val response = repository.getUpcoming(apikey, language(), page)

        return when (response.code()) {
            200 -> response.body() ?: throw Exception()
            204 -> throw Exception("No_content")
            in 400..500 -> throw Exception("HttpError_upcomingMovie")
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}