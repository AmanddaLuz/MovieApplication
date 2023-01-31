package com.amandaluz.network.usecase.searchusecase

import com.amandaluz.network.model.movie.Result

interface SearchMoviesUseCase {
    suspend fun getSearch(apikey: String, language: String, page: Int, query: String): List<Result>
}