package com.hugogarman.thesimpsonsapp.core.di

import com.google.gson.Gson
import com.hugogarman.thesimpsonsapp.R
import com.hugogarman.thesimpsonsapp.core.presentation.AppIntent
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        OkHttpClient.Builder().build()
    }
    single {
        AppIntent(androidContext())
    }
    single {
        Gson()
    }
    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.url_api))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}