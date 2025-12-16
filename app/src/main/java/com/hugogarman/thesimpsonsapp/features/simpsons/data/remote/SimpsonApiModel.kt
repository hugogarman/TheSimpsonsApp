package com.hugogarman.thesimpsonsapp.features.simpsons.data.remote

import com.google.gson.annotations.SerializedName

data class SimpsonApiModel(
    val id: Int,
    val age: Int,
    val name: String,
    val occupation: String,
    @SerializedName("portrait_path")
    val portraitPath: String,
    val phrases: List<String>
)