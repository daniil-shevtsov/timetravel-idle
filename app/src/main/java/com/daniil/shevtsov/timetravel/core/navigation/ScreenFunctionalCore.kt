package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.timetravel.feature.main.domain.toMainState
import com.daniil.shevtsov.timetravel.feature.main.domain.updateGameState

fun screenFunctionalCore(
    state: GameState,
    viewAction: ScreenViewAction,
): GameState {
    return when (viewAction) {
        is ScreenViewAction.Main -> mainFunctionalCore(
            state = state.toMainState(),
            viewAction = viewAction.action,
        ).updateGameState(currentState = state)
        is ScreenViewAction.General -> generalFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
    }
}
