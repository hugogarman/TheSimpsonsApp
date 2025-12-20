package com.hugogarman.thesimpsonsapp.features.simpsonsList.data

import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.local.SimpsonsSharedPreferencesLocalDataSource
import com.hugogarman.thesimpsonsapp.features.simpsonsList.data.remote.SimpsonsApiRemoteDataSource
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.SimpsonsRepository

class SimpsonsDataRepository(
    private val simpsonsApiRemoteDataSource: SimpsonsApiRemoteDataSource,
    private val simpsonsSharedPreferencesLocalDataSource: SimpsonsSharedPreferencesLocalDataSource
) : SimpsonsRepository {
    override suspend fun getAllSimpsons(): Result<List<Simpson>> {
        return if (simpsonsSharedPreferencesLocalDataSource.isCacheValid()) {
            Result.success(simpsonsSharedPreferencesLocalDataSource.findAll())
        } else {
            val remoteResult = simpsonsApiRemoteDataSource.getAllSimpsons()
            if (remoteResult.isSuccess) {
                remoteResult.getOrNull()?.let { simpsons ->
                    simpsonsSharedPreferencesLocalDataSource.save(simpsons)
                }
            }
            remoteResult
        }
    }
}