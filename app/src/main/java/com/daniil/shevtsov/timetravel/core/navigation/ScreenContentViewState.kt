package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewState

sealed class ScreenContentViewState {
    object Loading : ScreenContentViewState()
    data class Main(val state: MainViewState) : ScreenContentViewState()
}
