package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val contentState = when (state.currentScreen) {
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
    }

    return ScreenHostViewState(
        contentState = contentState,
    )
}
