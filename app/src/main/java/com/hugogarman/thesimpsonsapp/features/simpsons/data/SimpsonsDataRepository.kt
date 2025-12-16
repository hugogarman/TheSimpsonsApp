package com.hugogarman.thesimpsonsapp.features.simpsons.data

import android.util.Log
import com.hugogarman.thesimpsonsapp.features.simpsons.data.local.SimpsonsSharedPreferencesLocalDataSource
import com.hugogarman.thesimpsonsapp.features.simpsons.data.remote.SimpsonsApiRemoteDataSource
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.SimpsonsRepository

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