package com.hugogarman.thesimpsonsapp.features.simpsons.domain

import android.util.Log

class GetSimpsonsUseCase(private val simpsonsRepository: SimpsonsRepository) {
    suspend operator fun invoke(): Result<List<Simpson>> {
        val result = simpsonsRepository.getAllSimpsons()
        return result
    }
}