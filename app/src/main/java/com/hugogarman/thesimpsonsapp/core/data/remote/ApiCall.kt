package com.hugogarman.thesimpsonsapp.core.data.remote

import android.util.Log
import com.hugogarman.thesimpsonsapp.core.domain.ErrorApp
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
    val response: Response<T>
    try {
        response = call.invoke()
    } catch (exception: Throwable) {
        Log.e("apiCall", "Exception: ${exception.message}", exception)  // ← AÑADE ESTO
        return when (exception) {
            is ConnectException -> Result.failure(ErrorApp.InternetErrorApp)
            is SocketTimeoutException -> Result.failure(ErrorApp.InternetErrorApp)
            is UnknownHostException -> Result.failure(ErrorApp.ServerErrorApp)
            else -> Result.failure(ErrorApp.UnknownErrorApp)
        }
    }
    Log.d("apiCall", "Response: code=${response.code()}, success=${response.isSuccessful}, body=${response.body()}")  // ← AÑADE ESTO
    return if (response.isSuccessful && response.body() != null) {
        Result.success(response.body()!!)
    } else {
        when (response.code()) {
            else -> Result.failure(ErrorApp.UnknownErrorApp)
        }
    }
}