package com.amandaluz.movieapplication.view.categories.viewmodel

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.core.util.State
import com.amandaluz.core.util.State.Companion.error
import com.amandaluz.core.util.State.Companion.loading
import com.amandaluz.core.util.State.Companion.success
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
    private val getTrailer: TrailerUseCase,
    private val getTopRate: TopRateUseCase,
    private val getUpcoming: UpcomingUseCase,
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

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    fun getPopularMovies(apikey: String, language: String, page: Int) {
        viewModelScope.launch {
            try {
                _response.value = loading(true)
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

    fun getTopRate(page: Int) {
        viewModelScope.launch {
            try {

                val movies = withContext(ioDispatcher) {
                    getTopRate.getTopRate(page)
                }

                _rate.value = success(movies)
            } catch (e: Exception) {
                _rate.value = error(e)
            }
        }
    }

    fun getUpComing(page: Int) {
        viewModelScope.launch {
            try {

                val movies = withContext(ioDispatcher) {
                    getUpcoming.getUpcoming(page)
                }

                _coming.value = success(movies)
            } catch (e: Exception) {
                _coming.value = error(e)
            }
        }
    }

    fun getTrailerMovies(apikey: String, language: String, movieId: Int) {
        viewModelScope.launch {
            try {
                val trailer = withContext(ioDispatcher) {
                    getTrailer.getTrailerMovie(apikey, language, movieId)
                }
                _responseTrailer.value = success(trailer)
            } catch (e: Exception) {
                _responseTrailer.value = error(e)
            }
        }
    }

    fun hasInternet(context: Context?){
        val connectivityManager = context?.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val connection = connectivityManager?.getNetworkCapabilities(network)

        viewModelScope.launch {
            _isConnected.value = connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR)
                    )
        }
    }
}