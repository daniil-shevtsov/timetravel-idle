package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.main.domain.toMainState
import com.daniil.shevtsov.timetravel.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenViewState {
    return when (state.currentScreen) {
        Screen.Main -> ScreenViewState.Main(mapMainViewState(state.toMainState()))
    }
}
