package com.amandaluz.network.repository.searchrepository

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import retrofit2.Response

class SearchRepositoryImpl(private val api: MovieApi): SearchRepository {
    override suspend fun getSearch(
        apikey: String,
        language: String,
        page: Int,
        query: String
    ): Response<MovieResponse> =
        api.searchMovies(apikey, language, page, query)
}