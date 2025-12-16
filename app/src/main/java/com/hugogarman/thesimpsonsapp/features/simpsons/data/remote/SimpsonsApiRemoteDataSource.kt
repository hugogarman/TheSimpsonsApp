package com.hugogarman.thesimpsonsapp.features.simpsons.data.remote

import android.util.Log
import com.hugogarman.thesimpsonsapp.core.data.remote.apiCall
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson

class SimpsonsApiRemoteDataSource(private val apiService: SimpsonApiService) {
    suspend fun getAllSimpsons(): Result<List<Simpson>> {
        val result = apiCall { apiService.getAllSimpsons() }
        return result.map { response ->
            response.results.map { it.toModel() }
        }
    }
}