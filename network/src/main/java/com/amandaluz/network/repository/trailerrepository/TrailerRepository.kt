package com.amandaluz.network.repository.trailerrepository

import com.amandaluz.network.model.trailer.TrailerResponse
import retrofit2.Response

interface TrailerRepository {
    suspend fun getTrailer(apikey: String, language: String, movieId: Int): Response<TrailerResponse>
}