package com.amandaluz.network.repository.trailerrepository

import com.amandaluz.network.model.trailer.TrailerResponse
import com.amandaluz.network.service.MovieApi
import retrofit2.Response

class TrailerRepositoryImpl(private val api: MovieApi): TrailerRepository {
    override suspend fun getTrailer(
        apikey: String,
        language: String,
        movieId: Int
    ): Response<TrailerResponse> =
        api.getTrailerMovies(movie_id = movieId, api_key = apikey, language = language)
}