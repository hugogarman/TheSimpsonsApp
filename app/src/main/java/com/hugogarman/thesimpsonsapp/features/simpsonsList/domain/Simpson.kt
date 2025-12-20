package com.hugogarman.thesimpsonsapp.features.simpsonsList.domain

data class Simpson (
    val id: Int,
    val age: Int,
    val name: String,
    val occupation: String,
    val portraitPath: String,
    val phrases: List<String>
)