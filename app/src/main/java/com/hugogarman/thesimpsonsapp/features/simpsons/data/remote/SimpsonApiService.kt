package com.hugogarman.thesimpsonsapp.features.simpsons.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface SimpsonApiService {
    @GET("characters")
    suspend fun getAllSimpsons() : Response<SimpsonsResponse>
}