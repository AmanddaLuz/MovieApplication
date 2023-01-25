package com.amandaluz.movieapplication.di

import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import com.amandaluz.network.repository.trailerrepository.TrailerRepositoryImpl
import com.amandaluz.network.service.ApiService
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object TrailerComponent: KoinComponent {

    private val viewModel = module {
        viewModel{
            MovieViewModel(get(), get(), get())
        }
    }

    private val trailerUseCase = module {
        single<TrailerUseCase> {
            TrailerUseCaseImpl(get())
        }
    }

    private val trailerRepository = module {
        single<TrailerRepository> {
            TrailerRepositoryImpl(get())
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
        dispatcherModule,
        serviceConnector,
        trailerUseCase,
        trailerRepository
    )

}