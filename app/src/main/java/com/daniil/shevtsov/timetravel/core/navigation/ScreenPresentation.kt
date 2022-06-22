package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.drawerPresentation
import com.daniil.shevtsov.timetravel.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val drawerState = drawerPresentation(state)
    val contentState = when (state.currentScreen) {
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
    }

    return ScreenHostViewState(
        drawerState = drawerState,
        contentState = contentState,
    )
}
