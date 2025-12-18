package com.hugogarman.thesimpsonsapp.features.simpsonsList.di

import com.google.gson.Gson
import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.SimpsonsDataRepository
import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.local.SimpsonsSharedPreferencesLocalDataSource
import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.remote.SimpsonApiService
import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.remote.SimpsonsApiRemoteDataSource
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.GetSimpsonsUseCase
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.SimpsonsRepository
import com.hugogarman.thesimpsonsapp.features.simpsonsList.presentation.SimpsonsListViewModel
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