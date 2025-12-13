package com.hugogarman.thesimpsonsapp.core.domain

sealed class ErrorApp : Throwable() {
    data object InternetErrorApp : ErrorApp()
    data object ServerErrorApp : ErrorApp()
    data object UnknownErrorApp : ErrorApp()
    data object InvalidCredentialErrorApp : ErrorApp()
    data object NotFoundErrorApp : ErrorApp()
    data object ConflictErrorApp : ErrorApp()
}