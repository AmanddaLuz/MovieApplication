package com.amandaluz.movieapplication.di

import com.amandaluz.movieapplication.view.categories.viewmodel.CategoriesViewModel
import com.amandaluz.network.repository.categoryrepository.toprate.TopRateRepository
import com.amandaluz.network.repository.categoryrepository.toprate.TopRateRepositoryImpl
import com.amandaluz.network.repository.categoryrepository.upcoming.UpcomingRepository
import com.amandaluz.network.repository.categoryrepository.upcoming.UpcomingRepositoryImpl
import com.amandaluz.network.repository.movierepository.MovieRepository
import com.amandaluz.network.repository.movierepository.MovieRepositoryImpl
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import com.amandaluz.network.repository.trailerrepository.TrailerRepositoryImpl
import com.amandaluz.network.service.ApiService
import com.amandaluz.network.usecase.categoryusecase.toprate.TopRateUseCase
import com.amandaluz.network.usecase.categoryusecase.toprate.TopRateUseCaseImpl
import com.amandaluz.network.usecase.categoryusecase.upcoming.UpcomingUseCase
import com.amandaluz.network.usecase.categoryusecase.upcoming.UpcomingUseCaseImpl
import com.amandaluz.network.usecase.movieusecase.MovieUseCase
import com.amandaluz.network.usecase.movieusecase.MovieUseCaseImpl
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCase
import com.amandaluz.network.usecase.trailerusecase.TrailerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.unloadKoinModules
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CategoryComponent: KoinComponent {

    private val viewModel = module {
        viewModel {
            CategoriesViewModel(get(), get(), get(), get(), get())
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

    private val rateUseCase = module {
        single<TopRateUseCase> {
            TopRateUseCaseImpl(get())
        }
    }

    private val rateRepository = module {
        single<TopRateRepository> {
            TopRateRepositoryImpl(get())
        }
    }

    private val upcomingUseCase = module {
        single<UpcomingUseCase> {
            UpcomingUseCaseImpl(get())
        }
    }

    private val upcomingRepository = module {
        single<UpcomingRepository> {
            UpcomingRepositoryImpl(get())
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

    private val dispatcherModule = module {
        single{
            return@single Dispatchers.IO
        }
    }

    fun inject() = loadKoinModules(
        getModulesCategories()
    )

    fun unload() = unloadKoinModules(
        getModulesCategories()
    )

    fun getModulesCategories() = listOf(
        viewModel,
        movieRepository,
        movieUseCase,
        rateRepository,
        rateUseCase,
        upcomingRepository,
        upcomingUseCase,
        trailerRepository,
        trailerUseCase,
        dispatcherModule,
        serviceConnector
    )
}