package com.hugogarman.thesimpsonsapp.features.simpsonsList.domain

class GetSimpsonsUseCase(private val simpsonsRepository: SimpsonsRepository) {
    suspend operator fun invoke(): Result<List<Simpson>> {
        val result = simpsonsRepository.getAllSimpsons()
        return result
    }
}