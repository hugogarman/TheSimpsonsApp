package com.hugogarman.thesimpsonsapp.features.simpsonsList.data.remote

import com.hugogarman.thesimpsonsapp.core.data.remote.apiCall
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson

class SimpsonsApiRemoteDataSource(private val apiService: SimpsonApiService) {
    suspend fun getAllSimpsons(): Result<List<Simpson>> {
        val result = apiCall { apiService.getAllSimpsons() }
        return result.map { response ->
            response.results.map { it.toModel() }
        }
    }
}