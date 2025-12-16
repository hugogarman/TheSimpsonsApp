package com.hugogarman.thesimpsonsapp.features.simpsons.data.remote

import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson
fun SimpsonApiModel.toModel() : Simpson {
    val fullImageUrl = "https://cdn.thesimpsonsapi.com/500${this.portraitPath}"

    return Simpson(
        id = this.id,
        age = this.age,
        name = this.name,
        occupation = this.occupation,
        portraitPath = fullImageUrl,
        phrases = this.phrases
    )
}