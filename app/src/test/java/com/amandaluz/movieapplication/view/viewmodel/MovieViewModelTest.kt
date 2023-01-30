package com.amandaluz.movieapplication.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amandaluz.core.util.State
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
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

    private val movieObserver = mock(Observer::class.java) as Observer<State<List<Result>>>
    private val trailerObserver = mock(Observer::class.java) as Observer<State<List<ResultTrailer>>>

    private val exception = Exception()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return movieList when getPopularMovie is called`() = runTest {
        val movieList = listOf(result)

        //Arrange
        viewModel.response.observeForever(movieObserver)
        `when`(getMoviesUseCase.getPopularMovie("", "", 1)).thenReturn(movieList)

        //Act
        viewModel.getPopularMovies("", "", 1)
        val result = getMoviesUseCase.getPopularMovie("", "", 1)

        //Assert
        verify(movieObserver).onChanged(State.loading(true))
        verify(movieObserver).onChanged(State.success(result))
        verify(movieObserver).onChanged(State.loading(false))
    }

    @Test(expected = MockitoException::class)
    fun `should return an exception when getPopularMovie is called`() = runTest {
        //Arrange

        `when`(getMoviesUseCase.getPopularMovie("", "", 1)).thenThrow(exception)

        //Act
        viewModel.getPopularMovies("", "", 1)

        //Assert
        verify(movieObserver).onChanged(State.loading(true))
        verify(movieObserver).onChanged(State.error(exception))
        verify(movieObserver).onChanged(State.loading(false))
    }

    @Test
    fun `should return trailerList when getTrailerMovie is called`() = runTest {
        //Arrange
        viewModel.responseTrailer.observeForever(trailerObserver)
        `when`(getTrailerUseCase.getTrailerMovie("", "", 1)).thenReturn(listOf(resultTrailer))

        //Act
        viewModel.getTrailerMovies("", "", 1)
        val result = getTrailerUseCase.getTrailerMovie("", "", 1)


        //Assert
        verify(trailerObserver).onChanged(State.loading(true))
        verify(trailerObserver).onChanged(State.success(result))
        verify(trailerObserver).onChanged(State.loading(false))
    }

    @Test(expected = MockitoException::class)
    fun `should throw an exception when getPopularMovie is called`() = runTest {
        //Arrange
        viewModel.responseTrailer.observeForever(trailerObserver)
        `when`(getTrailerUseCase.getTrailerMovie("", "", 1)).thenThrow(exception)

        //Act
        viewModel.getTrailerMovies("", "", 297761)

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
}