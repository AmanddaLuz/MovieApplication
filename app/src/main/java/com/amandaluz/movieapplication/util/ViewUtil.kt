package com.amandaluz.movieapplication.util

import androidx.fragment.app.FragmentManager
import com.amandaluz.hawk.ModuleHawk
import com.amandaluz.hawk.MovieKeys
import com.amandaluz.hawk.movierepository.MovieCacheRepositoryImpl
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.BottomSheetDetail

fun bottomSheetDetail(
    manager: FragmentManager,
    movie: Result,
    hasInternet: () -> Unit,
    favorite: () -> Unit,
    checked: Boolean
) {
    val bottomSheetDetail = BottomSheetDetail()
    bottomSheetDetail.show(manager, "BOTTOM_SHEET - DETAIL")
    bottomSheetDetail.viewTargetDetail(movie.poster_path)
    bottomSheetDetail.viewTargetPoster(movie.poster_path)
    bottomSheetDetail.setTitle("Nota: ${movie.vote_average}")
    bottomSheetDetail.setDescription("Votos: ${movie.vote_count}")
    bottomSheetDetail.setDetail(movie.overview)
    bottomSheetDetail.buttonCloseAction { it.dismiss() }
    bottomSheetDetail.buttonConfirmAction {
        hasInternet.invoke()
        it.dismiss()
    }
    bottomSheetDetail.buttonFavoriteAction {
        favorite.invoke()
        bottomSheetDetail.isFavorite(checked)
    }
}

fun getTrailerKey(isConnection: Boolean, trailerList: List<ResultTrailer>,trailerResponse: List<ResultTrailer> ): String? {
    return if (isConnection) {
        var key: String? = null
        trailerList.forEach {
            key = it.key
        }
        key
    } else {
        var key: String? = null
        trailerResponse.forEach {
            key = it.key
        }
        key
    }
}

fun verifyCacheMovies(yes: () -> Unit, no: () -> Unit) {
    if (ModuleHawk.contains(MovieKeys.MOVIES)) {
        yes.invoke()
    } else
        no.invoke()
}

fun addCacheMovies(moviesResult: List<Result>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.MOVIES, moviesResult)
}

fun addCacheTrailer(trailerResult: List<ResultTrailer>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.TRAILERS, trailerResult)
}

fun getMovieCache(): List<Result> {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    return movieCache.get(MovieKeys.MOVIES)
}

fun getTrailerCache(): List<ResultTrailer> {
    val trailerCache = MovieCacheRepositoryImpl(ModuleHawk)
    return trailerCache.get(MovieKeys.TRAILERS)
}

fun verifyCacheFavorites(yes: () -> Unit, no: () -> Unit) {
    if (ModuleHawk.contains(MovieKeys.FAVORITES)) {
        yes.invoke()
    } else
        no.invoke()
}

fun verifyCacheImageButton(): Int {
    return if (ModuleHawk.contains(MovieKeys.FAVORITES)) {
        com.amandaluz.ui.R.drawable.ic_favorite_button_selected
    } else {
        com.amandaluz.ui.R.drawable.ic_favorite_button_unselected
    }
}

fun verifyImageButton(movie: Result, list: MutableList<Result>): Int {
    return if (list != null && list.contains(movie)) {
        com.amandaluz.ui.R.drawable.ic_favorite_button_selected
    } else {
        com.amandaluz.ui.R.drawable.ic_favorite_button_unselected
    }
}

fun addCacheFavorites(moviesResult: List<Result>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.FAVORITES, moviesResult)
}

fun getFavoritesCache(): List<Result> {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    return movieCache.get(MovieKeys.FAVORITES)
}
