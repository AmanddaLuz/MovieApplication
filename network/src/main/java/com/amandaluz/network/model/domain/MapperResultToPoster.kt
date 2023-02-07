package com.amandaluz.network.model.domain

import java.io.Serializable
import com.amandaluz.network.model.movie.Result

class MapperResultToPoster {
    companion object {
        operator fun invoke(results: List<Result>): List<PosterDomain> {
            return results.map { result: Result ->
                PosterDomain(
                    title = result.title,
                    poster_path = result.poster_path
                )
            }
        }
    }
}

data class PosterDomain(
    val title: String,
    val poster_path: String?
): Serializable