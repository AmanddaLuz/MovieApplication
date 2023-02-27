package com.amandaluz.movieapplication.view.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amandaluz.core.util.State
import com.amandaluz.network.model.trailer.ResultTrailer
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
internal class FavoriteViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val getTrailerUseCase = mock(TrailerUseCase::class.java)

    private val ioDispatcher = Dispatchers.IO

    private val viewModel = FavoriteViewModel(getTrailerUseCase, ioDispatcher)

    private val trailerObserver = mock(Observer::class.java) as Observer<State<List<ResultTrailer>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
    fun `should throw an exception when getTrailerMovie is called`() = runTest {
        val exception = Exception()

        //Arrange
        viewModel.responseTrailer.observeForever(trailerObserver)
        `when`(getTrailerUseCase.getTrailerMovie("", "", 1)).thenThrow(exception)

        //Act
        viewModel.getTrailerMovies("", "", 297761)

        //Assert
        verify(trailerObserver).onChanged(State.error(exception))
    }

    private val resultTrailer = ResultTrailer(
        id = "",
        iso6391 = "",
        iso31661 = "",
        key = "",
        name = "",
        official = true,
        publishedAt = "",
        site = "",
        size = 1080,
        type = ""
    )
}