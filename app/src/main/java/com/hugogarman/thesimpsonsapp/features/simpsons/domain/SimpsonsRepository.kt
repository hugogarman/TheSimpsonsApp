package com.hugogarman.thesimpsonsapp.features.simpsons.domain

interface SimpsonsRepository {
    suspend fun getAllSimpsons() : Result<List<Simpson>>
}