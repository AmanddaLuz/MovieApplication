package com.amandaluz.movieapplication.di

import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.repository.movierepository.MovieRepository
import com.amandaluz.network.repository.movierepository.MovieRepositoryImpl
import com.amandaluz.network.service.ApiService
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.movieusecase.MovieUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object MovieComponent: KoinComponent {

    private val viewModel = module {
        viewModel {
            MovieViewModel(get(), get(), get())
        }
    }

    private val movieUseCase = module {
        single<MovieUseCase> {
            MovieUseCaseImpl(get())
        }
    }

    private val movieRepository = module {
        single<MovieRepository> {
            MovieRepositoryImpl(get())
        }
    }

    private val serviceConnector = module {
        single{
            ApiService.service
        }
    }

    private val dispatcherModule = module {
        single{
            return@single Dispatchers.IO
        }
    }

    fun inject() = loadKoinModules(
        getModules()
    )

    fun getModules() = listOf(
        viewModel,
        movieRepository,
        movieUseCase,
        dispatcherModule,
        serviceConnector
    )
}