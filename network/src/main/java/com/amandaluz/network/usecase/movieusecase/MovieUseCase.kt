package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result

interface MovieUseCase {
    suspend fun getPopularMovie(apikey: String, language: String, page: Int): List<Result>?

    suspend fun getPopularMovieResponse(apikey: String, language: String, page: Int): MovieResponse?

}