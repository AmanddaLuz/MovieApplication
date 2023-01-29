package com.amandaluz.movieapplication.util

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.amandaluz.core.util.linkPathNull
import com.amandaluz.hawk.ModuleHawk
import com.amandaluz.hawk.MovieKeys
import com.amandaluz.hawk.movierepository.MovieCacheRepositoryImpl
import com.amandaluz.movieapplication.R
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.BottomSheetDetail

fun callBottomSheet(
    bottomSheetDetail: BottomSheetDetail,
    movie: Result,
    context: Context,
    buttonConfirm: () -> Unit,
    buttonFavorite: () -> Unit,
    imageButton: Int,
    manager: FragmentManager,
    tag: String
) {
    bottomSheetDetail.viewTargetPoster(validatePoster(movie))
    bottomSheetDetail.viewTargetDetail(validatePoster(movie))
    bottomSheetDetail.setTitle(validateDescription(movie.title, context))
    bottomSheetDetail.setNota(buildString {
        append(context.getString(R.string.average))
        append(movie.vote_average)
    })
    bottomSheetDetail.setDescription(buildString {
        append(context.getString(R.string.votes))
        append(movie.vote_count)
    })
    bottomSheetDetail.setDetail(validateDescription(movie.overview, context))
    bottomSheetDetail.buttonCloseAction { it.dismiss() }
    bottomSheetDetail.buttonConfirmAction {
        buttonConfirm.invoke()
        it.dismiss()
    }
    bottomSheetDetail.buttonFavoriteAction {
        buttonFavorite.invoke()
    }
    bottomSheetDetail.setImageButton(imageButton/*verifyImageButton(movie, favoriteList)*/)
    bottomSheetDetail.show(manager, tag)
}

fun getHomeTrailerKey(isConnection: Boolean, trailerList: List<ResultTrailer>, trailerResponse: List<ResultTrailer> ): String? {
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

fun validatePoster(movie: Result) =
    if (movie.poster_path.isNullOrEmpty()) linkPathNull()
    else movie.poster_path

fun validateDescription(description: String, context: Context) =
    if (description.isNullOrEmpty()) context.getString(R.string.label_indisponible)
    else description
