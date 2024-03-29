package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.repository.movierepository.MovieRepository

class MovieUseCaseImpl(
    private val repository: MovieRepository
) : MovieUseCase {

    override suspend fun getPopularMovie(apikey: String, language: String, page: Int): List<Result> {
        val response = repository.getMovie(apikey, language, page)

        return when (response.code()) {
            200 -> response.body()?.results ?: throw Exception()
            204 -> throw Exception("No_content")
            in 400..500 -> throw Exception("HttpError_popularMovie")
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}
