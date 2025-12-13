package com.hugogarman.thesimpsonsapp.core.presentation.ext

import com.hugogarman.thesimpsonsapp.core.domain.ErrorApp
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toErrorApp() : ErrorApp {
    return when (this) {
        is HttpException -> this.toHttpError()
        is UnknownHostException,
        is ConnectException,
        is SocketTimeoutException -> ErrorApp.InternetErrorApp
        else -> ErrorApp.UnknownErrorApp
    }
}

private fun HttpException.toHttpError() : ErrorApp {
    return when (code()) {
        401 -> ErrorApp.InvalidCredentialErrorApp
        404 -> ErrorApp.NotFoundErrorApp
        409 -> ErrorApp.ConflictErrorApp
        in 500..599 -> ErrorApp.ServerErrorApp
        else -> ErrorApp.UnknownErrorApp
    }
}