package com.amandaluz.movieapplication.view.categories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.core.util.State
import com.amandaluz.core.util.State.Companion.error
import com.amandaluz.core.util.State.Companion.loading
import com.amandaluz.core.util.State.Companion.success
import com.amandaluz.core.util.apikey
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.categoryusecase.toprate.TopRateUseCase
import com.amandaluz.network.usecase.categoryusecase.upcoming.UpcomingUseCase
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(
    private val getMovies: MovieUseCase,
    private val getTopRate: TopRateUseCase,
    private val getUpcoming: UpcomingUseCase,
    private val getTrailer: TrailerUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _response = MutableLiveData<State<List<Result>>>()
    val response: LiveData<State<List<Result>>> = _response

    private var _rate = MutableLiveData<State<MovieResponse>>()
    val rate: LiveData<State<MovieResponse>> = _rate

    private var _coming = MutableLiveData<State<MovieResponse>>()
    val coming: LiveData<State<MovieResponse>> = _coming

    private var _responseTrailer = MutableLiveData<State<List<ResultTrailer>>>()
    val responseTrailer: LiveData<State<List<ResultTrailer>>> = _responseTrailer

    fun getPopularMovies(apikey: String, language: String, page: Int) {
        viewModelScope.launch {
            try {
                _response.value = loading(true)
                val movies = withContext(ioDispatcher) {
                    getMovies.getPopularMovie(apikey, language, page)
                }
                _response.value = success(movies)
            } catch (e: Exception) {
                _response.value = loading(false)
            }
        }
    }

    fun getTopRate(page: Int) {
        viewModelScope.launch {
            try {

                val movies = withContext(ioDispatcher) {
                    getTopRate.getTopRate(apikey(), page)
                }
                _response.value = loading(false)
                _rate.value = success(movies)
            } catch (e: Exception) {
                _response.value = loading(false)
                _rate.value = error(e)
            }
        }
    }

    fun getUpComing(apikey: String, page: Int) {
        viewModelScope.launch {
            try {

                val movies = withContext(ioDispatcher) {
                    getUpcoming.getUpcoming(apikey ,page)
                }
                _coming.value = success(movies)
            } catch (e: Exception) {
                _response.value = loading(false)
                _coming.value = error(e)
            }
        }
    }

    fun getTrailerMovies(apikey: String, language: String, movieId: Int) {
        viewModelScope.launch {
            try {
                _responseTrailer.value = loading(true)
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
}