package com.amandaluz.movieapplication.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.core.util.State
import com.amandaluz.core.util.State.Companion.error
import com.amandaluz.core.util.State.Companion.loading
import com.amandaluz.core.util.State.Companion.success
import com.amandaluz.core.util.url.language
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.searchusecase.SearchMoviesUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieViewModel(
    private val getMovies: MovieUseCase,
    private val getTrailer: TrailerUseCase,
    private val searchMovie: SearchMoviesUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _response = MutableLiveData<State<List<Result>>>()
    val response: LiveData<State<List<Result>>> = _response

    private var _responseTrailer = MutableLiveData<State<List<ResultTrailer>>>()
    val responseTrailer: LiveData<State<List<ResultTrailer>>> = _responseTrailer

    private var _search = MutableLiveData<State<List<Result>>>()
    val search: LiveData<State<List<Result>>> = _search

    fun getPopularMovies(apikey: String, language: String, page: Int) {
        viewModelScope.launch {
            _response.value = loading(true)
            try {
                val movies = withContext(ioDispatcher) {
                    getMovies.getPopularMovie(apikey, language, page)
                }
                _response.value = loading(false)
                _response.value = success(movies)
            } catch (e: Exception) {
                _response.value = error(e)
                _response.value = loading(false)
            }
        }
    }

    fun getTrailerMovies(apikey: String, language: String, movieId: Int) {
        viewModelScope.launch {
            _responseTrailer.value = loading(true)
            try {
                val trailer = withContext(ioDispatcher) {
                    getTrailer.getTrailerMovie(apikey, language, movieId)
                }
                _responseTrailer.value = loading(false)
                _responseTrailer.value = success(trailer)
            } catch (e: Exception) {
                _responseTrailer.value = loading(false)
                _responseTrailer.value = error(e)
            }
        }
    }

    fun searchMovie(apikey: String, query: String, page: Int) {
        viewModelScope.launch {
            _search.value = loading(true)
            try {
                val movies = withContext(ioDispatcher) {
                    searchMovie.getSearch(apikey, language(), page, query)
                }
                _search.value = success(movies)
                _search.value = loading(false)
            } catch (e: Exception) {
                _search.value = error(e)
                _search.value = loading(false)
            }
        }
    }
}