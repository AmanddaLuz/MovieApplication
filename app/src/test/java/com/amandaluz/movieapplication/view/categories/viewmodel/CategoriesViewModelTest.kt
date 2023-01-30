package com.amandaluz.movieapplication.view.categories.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amandaluz.network.usecase.categoryusecase.toprate.TopRateUseCase
import com.amandaluz.network.usecase.categoryusecase.upcoming.UpcomingUseCase
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CategoriesViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val getMoviesUseCase = Mockito.mock(MovieUseCase::class.java)
    private val getTopRateUseCase = Mockito.mock(TopRateUseCase::class.java)
    private val getUpComingUseCase = Mockito.mock(UpcomingUseCase::class.java)
    private val viewModel = CategoriesViewModel(getMoviesUseCase, getTopRateUseCase, getUpComingUseCase, dispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}