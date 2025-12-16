package com.hugogarman.thesimpsonsapp.features.simpsons.di

import com.google.gson.Gson
import com.hugogarman.thesimpsonsapp.features.simpsons.data.SimpsonsDataRepository
import com.hugogarman.thesimpsonsapp.features.simpsons.data.local.SimpsonsSharedPreferencesLocalDataSource
import com.hugogarman.thesimpsonsapp.features.simpsons.data.remote.SimpsonApiService
import com.hugogarman.thesimpsonsapp.features.simpsons.data.remote.SimpsonsApiRemoteDataSource
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.GetSimpsonsUseCase
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.SimpsonsRepository
import com.hugogarman.thesimpsonsapp.features.simpsons.presentation.SimpsonsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val simpsonsModule = module {
    single {
        Gson()
    }
    single {
        get<Retrofit>().create(SimpsonApiService::class.java)
    }
    single {
        SimpsonsApiRemoteDataSource(get())
    }
    single {
        SimpsonsSharedPreferencesLocalDataSource(get(), get())
    }
    single<SimpsonsRepository> {
        SimpsonsDataRepository(get(), get())
    }
    factory {
        GetSimpsonsUseCase(get())
    }
    viewModel {
        SimpsonsListViewModel(get())
    }
}