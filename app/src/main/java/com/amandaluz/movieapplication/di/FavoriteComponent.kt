package com.amandaluz.movieapplication.di

import com.amandaluz.movieapplication.view.favorite.viewmodel.FavoriteViewModel
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import com.amandaluz.network.repository.trailerrepository.TrailerRepositoryImpl
import com.amandaluz.network.service.ApiService
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

object FavoriteComponent: KoinComponent {

    private val viewModel = module {
        viewModel {
            FavoriteViewModel(get(), get())
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
        getModulesFavorite()
    )

    fun unload() = unloadKoinModules(
        getModulesFavorite()
    )

    private fun getModulesFavorite() = listOf(
        viewModel,
        trailerRepository,
        trailerUseCase,
        dispatcherModule,
        serviceConnector
    )
}