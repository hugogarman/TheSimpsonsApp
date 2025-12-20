package com.hugogarman.thesimpsonsapp.features.simpsonsList.domain

interface SimpsonsRepository {
    suspend fun getAllSimpsons() : Result<List<Simpson>>
}