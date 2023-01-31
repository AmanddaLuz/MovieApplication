package com.amandaluz.network.usecase.searchusecase

import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.repository.searchrepository.SearchRepository
import retrofit2.HttpException

class SearchMoviesUseCaseImpl(private val repository: SearchRepository) :
    SearchMoviesUseCase {
    override suspend fun getSearch(apikey: String, language: String, page: Int, query: String): List<Result> {
        val response = repository.getSearch(apikey, language, page, query)

        return when (response.code()) {
            200 -> response.body()?.results ?: throw Exception("No_content")
            in 400..500 -> throw HttpException(response)
            else -> throw IllegalArgumentException()
        }
    }
}
