package com.amandaluz.movieapplication.view.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.core.util.State
import com.amandaluz.core.util.State.Companion.error
import com.amandaluz.core.util.State.Companion.loading
import com.amandaluz.core.util.State.Companion.success
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val getTrailer: TrailerUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _responseTrailer = MutableLiveData<State<List<ResultTrailer>>>()
    val responseTrailer: LiveData<State<List<ResultTrailer>>> = _responseTrailer

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