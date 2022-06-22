package com.daniil.shevtsov.timetravel.feature.main.presentation


sealed class MainViewState {
    object Loading : MainViewState()

    object Success : MainViewState()
}
