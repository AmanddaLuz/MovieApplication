package com.amandaluz.movieapplication.util.cache

import com.amandaluz.hawk.ModuleHawk
import com.amandaluz.hawk.MovieKeys
import com.amandaluz.hawk.movierepository.MovieCacheRepositoryImpl
import com.amandaluz.network.model.category.CategoryItem
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer

fun verifyCacheMovies(yes: () -> Unit, no: () -> Unit) {
    if (ModuleHawk.contains(MovieKeys.MOVIES)) {
        yes.invoke()
    } else
        no.invoke()
}

fun verifyCategoriesMovies(yes: () -> Unit) {
    if (ModuleHawk.contains(MovieKeys.CATEGORIES)) yes.invoke()
}

fun addCacheMovies(moviesResult: List<Result>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.MOVIES, moviesResult)
}

fun addCategoriesMovies(categoryResult: MutableList<CategoryItem>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.CATEGORIES, categoryResult)
}


fun addCacheTrailer(trailerResult: List<ResultTrailer>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.TRAILERS, trailerResult)
}

fun getMovieCache(): List<Result> {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    return movieCache.get(MovieKeys.MOVIES)
}

fun getCategoriesCache(): List<CategoryItem> {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    return movieCache.get(MovieKeys.CATEGORIES)
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

fun addCacheFavorites(moviesResult: List<Result>) {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    movieCache.add(MovieKeys.FAVORITES, moviesResult)
}

fun getFavoritesCache(): List<Result> {
    val movieCache = MovieCacheRepositoryImpl(ModuleHawk)
    return movieCache.get(MovieKeys.FAVORITES)
}