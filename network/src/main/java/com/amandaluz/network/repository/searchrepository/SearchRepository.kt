package com.amandaluz.network.repository.searchrepository

import com.amandaluz.network.model.movie.MovieResponse
import retrofit2.Response

interface SearchRepository {
    suspend fun getSearch(apikey: String, language: String, page: Int, query: String): Response<MovieResponse>
}