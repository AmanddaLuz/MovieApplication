package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.repository.movierepository.MovieRepository
import timber.log.Timber

class MovieUseCaseImpl(
    private val repository: MovieRepository
) : MovieUseCase {

    override suspend fun getPopularMovie(apikey: String, language: String, page: Int): List<Result>? {
        val response = repository.getMovie(apikey, language, page)
        return when (response.code()) {
            200 -> response.body()?.results
            else -> {
                Timber.tag(
                    "GET_MOVIE_RESPONSE:${response.code()} - ${response.errorBody()}"
                )
                throw IllegalArgumentException()
            }
        }
    }

    override suspend fun getPopularMovieResponse(
        apikey: String,
        language: String,
        page: Int
    ): MovieResponse? {
        val response = repository.getMovie(apikey, language, page)
        return when (response.code()) {
            200 -> response.body()
            else -> {
                Timber.tag(
                    "GET_MOVIE_RESPONSE:${response.code()} - ${response.errorBody()}"
                )
                throw IllegalArgumentException()
            }
        }
    }
}
