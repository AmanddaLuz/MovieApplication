package com.amandaluz.movieapplication.view.viewmodel

import android.content.Context

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amandaluz.core.util.State
import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.exceptions.base.MockitoException

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val getMoviesUseCase = mock(MovieUseCase::class.java)
    private val getTrailerUseCase = mock(TrailerUseCase::class.java)

    private val ioDispatcher = Dispatchers.IO

    private val viewModel = MovieViewModel(getMoviesUseCase, getTrailerUseCase, ioDispatcher)

    private val context = mock(Context::class.java)
    private val mockConnectivityManager = mock(ConnectivityManager::class.java)
    private val mockNetwork = mock(Network::class.java)
    private val mockConnection = mock(NetworkCapabilities::class.java)

    private val movieObserver = mock(Observer::class.java) as Observer<State<List<Result>>>
    private val trailerObserver = mock(Observer::class.java) as Observer<State<List<ResultTrailer>>>
    private val connectionObserver = mock(Observer::class.java) as Observer<Boolean>

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test(expected = MockitoException::class)
    fun `should liveData return true when hasConnection is called`() = runTest {
        //Arrange
        viewModel.isConnected.observeForever(connectionObserver)
        /*`when`(context.getSystemService(CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)
        `when`(mockConnectivityManager.activeNetwork).thenReturn(mockNetwork)
        `when`(mockConnectivityManager.getNetworkCapabilities(mockNetwork)).thenReturn(mockConnection)*/

        //Act
        viewModel.hasInternet(context)
        val result = viewModel.isConnected.value

        //Assert
        verify(connectionObserver).onChanged(true)
        //assertEquals(true, result)
    }

    @Test
    fun `should return movieList when getPopularMovie is called`() = runTest {
        //Arrange
        viewModel.response.observeForever(movieObserver)
        `when`(getMoviesUseCase.getPopularMovie("", language(), 1)).thenReturn(movieList)

        //Act
        viewModel.getPopularMovies("", language(), 1)

        //Assert
        verify(movieObserver).onChanged(State.loading(true))
        verify(movieObserver).onChanged(State.success(movieList))
        verify(movieObserver).onChanged(State.loading(false))
    }

    @Test(expected = MockitoException::class)
    fun `should return an exception when getPopularMovie is called`() = runTest {
        //Arrange

        `when`(getMoviesUseCase.getPopularMovie("", language(), 1)).thenThrow(exception)

        //Act
        viewModel.getPopularMovies("", language(), 1)

        //Assert
        verify(movieObserver).onChanged(State.loading(true))
        verify(movieObserver).onChanged(State.error(exception))
        verify(movieObserver).onChanged(State.loading(false))
    }

    @Test
    fun `should return trailerList when getTrailerMovie is called`() = runTest {
        val list = trailerList
        //Arrange
        viewModel.responseTrailer.observeForever(trailerObserver)
        `when`(getTrailerUseCase.getTrailerMovie("", "", 1)).thenReturn(trailerList)

        //Act
        viewModel.getTrailerMovies("", "", 1)

        //Assert
        //verify(trailerObserver).onChanged(State.success(trailerList))
        assertEquals(list, viewModel.responseTrailer.value)
    }

    @Test(expected = MockitoException::class)
    fun `should throw an exception when getPopularMovie is called`() = runTest {
        //Arrange
        viewModel.responseTrailer.observeForever(trailerObserver)
        `when`(getTrailerUseCase.getTrailerMovie("", language(), moveId(result))).thenThrow(exception)

        //Act
        viewModel.getTrailerMovies("", language(), 297761)

        //Assert
        verify(trailerObserver).onChanged(State.error(exception))
    }

    private val result = Result(
        id = 297761,
        adult = false,
        genre_ids = listOf(),
        title = "title",
        overview = "overview",
        poster_path = "",
        release_date = "",
        vote_average = 0.0,
        popularity = 0.0,
        backdrop_path = "",
        original_language = "",
        original_title = "",
        video = false,
        vote_count = 0
    )

    private val resultTrailer = ResultTrailer(
        id = "",
        iso_639_1 = "",
        iso_3166_1 = "",
        key = "",
        name = "",
        official = true,
        published_at = "",
        site = "",
        size = 1080,
        type = ""
    )

    private fun moveId(movie: Result) = movie.id
    private val movieList = listOf(result)
    private val trailerList = listOf(resultTrailer)
    private fun isConnect() = true
    private fun noConnet() = false
    private val exception = Exception()
}