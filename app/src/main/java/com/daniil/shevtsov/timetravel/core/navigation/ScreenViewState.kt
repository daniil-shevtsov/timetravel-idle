package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewState

sealed class ScreenViewState {
    data class Main(val state: MainViewState): ScreenViewState()
}
