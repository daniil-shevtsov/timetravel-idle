package com.daniil.shevtsov.timetravel.feature.main.presentation


sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val kek: String,
    ) : MainViewState()
}
