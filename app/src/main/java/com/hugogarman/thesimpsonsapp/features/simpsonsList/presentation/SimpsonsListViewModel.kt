package com.hugogarman.thesimpsonsapp.features.simpsonsList.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hugogarman.thesimpsonsapp.core.domain.ErrorApp
import com.hugogarman.thesimpsonsapp.core.presentation.ext.toErrorApp
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.GetSimpsonsUseCase
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson
import kotlinx.coroutines.launch

class SimpsonsListViewModel(private val getSimpsonsUseCase: GetSimpsonsUseCase) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun loadSimpsons() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch {
            getSimpsonsUseCase().fold(
                { simpsons ->
                    onSimpsonsSuccess(simpsons)
                },
                { throwable ->
                    onSimpsonsFailure(throwable.toErrorApp())
                }
            )
        }
    }

    private fun onSimpsonsSuccess(simpsons: List<Simpson>) {
        _uiState.postValue(
            UiState(isLoading = false, simpsonsList = simpsons, errorApp = null)
        )
    }

    private fun onSimpsonsFailure(error: ErrorApp) {
        _uiState.postValue(
            UiState(isLoading = false, simpsonsList = null, errorApp = error)
        )
    }

    data class UiState(
        val errorApp: ErrorApp? = null,
        val isLoading: Boolean = false,
        val simpsonsList: List<Simpson>? = null
    )
}