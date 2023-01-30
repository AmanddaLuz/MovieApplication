package com.amandaluz.movieapplication.view.categories.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amandaluz.core.util.State
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.network.usecase.categoryusecase.toprate.TopRateUseCase
import com.amandaluz.network.usecase.categoryusecase.upcoming.UpcomingUseCase
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
class CategoriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val getMoviesUseCase = mock(MovieUseCase::class.java)
    private val getTopRateUseCase = mock(TopRateUseCase::class.java)
    private val getUpComingUseCase = mock(UpcomingUseCase::class.java)
    private val getTrailerUseCase = mock(TrailerUseCase::class.java)

    private val ioDispatcher = Dispatchers.IO

    private val viewModel =
        CategoriesViewModel(getMoviesUseCase, getTopRateUseCase, getUpComingUseCase, getTrailerUseCase, ioDispatcher)

    private val movieObserver = mock(Observer::class.java) as Observer<State<List<Result>>>
    private val rateObserver = mock(Observer::class.java) as Observer<State<MovieResponse>>
    private val upcomingObserver = mock(Observer::class.java) as Observer<State<MovieResponse>>
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
    fun `should return topRateList when getTopRate is called`() = runTest {
        val response = MovieResponse(1, listOf(), 1, 1)
        //Arrange
        viewModel.response.observeForever(movieObserver)
        viewModel.rate.observeForever(rateObserver)
        `when`(getTopRateUseCase.getTopRate(1)).thenReturn(response)

        //Act
        viewModel.getTopRate(1)
        val result = getTopRateUseCase.getTopRate(1)


        //Assert
        verify(movieObserver).onChanged(State.loading(false))
        verify(rateObserver).onChanged(State.success(result))
    }

    @Test(expected = MockitoException::class)
    fun `should throw an exception when getTopRate is called`() = runTest {
        //Arrange
        viewModel.response.observeForever(movieObserver)
        viewModel.rate.observeForever(rateObserver)
        `when`(getTopRateUseCase.getTopRate(1)).thenThrow(exception)

        //Act
        viewModel.getTopRate(1)

        //Assert
        verify(movieObserver).onChanged(State.loading(false))
        verify(rateObserver).onChanged(State.error(exception))
    }

    @Test
    fun `should return upcomingList when getUpcoming is called`() = runTest {
        val response = MovieResponse(1, listOf(), 1, 1)
        //Arrange
        viewModel.response.observeForever(movieObserver)
        viewModel.coming.observeForever(upcomingObserver)
        `when`(getUpComingUseCase.getUpcoming(1)).thenReturn(response)

        //Act
        viewModel.getUpComing(1)
        val result = getUpComingUseCase.getUpcoming(1)

        //Assert
        verify(upcomingObserver).onChanged(State.success(result))
    }

    @Test(expected = MockitoException::class)
    fun `should throw an exception when getUpcoming is called`() = runTest {
        //Arrange
        viewModel.response.observeForever(movieObserver)
        viewModel.coming.observeForever(upcomingObserver)
        `when`(getUpComingUseCase.getUpcoming(1)).thenThrow(exception)

        //Act
        viewModel.getUpComing(1)

        //Assert
        verify(movieObserver).onChanged(State.loading(false))
        verify(rateObserver).onChanged(State.error(exception))
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