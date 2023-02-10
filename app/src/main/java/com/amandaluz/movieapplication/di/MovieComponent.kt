package com.amandaluz.movieapplication.di

import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.repository.movierepository.MovieRepository
import com.amandaluz.network.repository.movierepository.MovieRepositoryImpl
import com.amandaluz.network.repository.searchrepository.SearchRepository
import com.amandaluz.network.repository.searchrepository.SearchRepositoryImpl
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import com.amandaluz.network.repository.trailerrepository.TrailerRepositoryImpl
import com.amandaluz.network.service.ApiService
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.movieusecase.MovieUseCaseImpl
import com.amandaluz.network.usecase.searchusecase.SearchMoviesUseCase
import com.amandaluz.network.usecase.searchusecase.SearchMoviesUseCaseImpl
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object MovieComponent: KoinComponent {

    private val viewModel = module {
        viewModel {
            MovieViewModel(get(), get(), get(), get())
        }
    }

    private val movieUseCase = module {
        factory<MovieUseCase> {
            MovieUseCaseImpl(get())
        }
    }

    private val movieRepository = module {
        factory<MovieRepository> {
            MovieRepositoryImpl(get())
        }
    }

    private val trailerUseCase = module {
        factory<TrailerUseCase> {
            TrailerUseCaseImpl(get())
        }
    }

    private val trailerRepository = module {
        factory<TrailerRepository> {
            TrailerRepositoryImpl(get())
        }
    }

    private val searchUseCase = module {
        single<SearchMoviesUseCase> {
            SearchMoviesUseCaseImpl(get())
        }
    }

    private val searchRepository = module {
        single<SearchRepository>() {
            SearchRepositoryImpl(get())
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
        getModulesHome()
    )

    fun unload(){
        getModulesHome()
    }

    fun getModulesHome() = listOf(
        viewModel,
        movieRepository,
        movieUseCase,
        trailerRepository,
        trailerUseCase,
        searchUseCase,
        searchRepository,
        dispatcherModule,
        serviceConnector
    )
}