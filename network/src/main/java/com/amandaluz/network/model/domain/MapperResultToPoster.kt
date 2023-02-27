package com.amandaluz.network.model.domain

import java.io.Serializable
import com.amandaluz.network.model.movie.Result

class MapperResultToPoster {
    companion object {
        operator fun invoke(results: Result): PosterDomain {

            return PosterDomain(
                results.title ?: "",
                results.posterPath
            )

        }
    }
}

data class PosterDomain(
    val title: String,
    val poster_path: String?
): Serializable

fun poster(movie: Result): MutableList<PosterDomain> {
    return mutableListOf(
        PosterDomain("Aqui você pode encontrar seus filmes preferidos!", movie.posterPath),
        PosterDomain("E também os filmes que estão para serem lançados!", null),
        PosterDomain("Classifique os filmes nos enviando sua nota de 0 a 10!", movie.posterPath)
    )
}