package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.domain.drawerFunctionalCore
import com.daniil.shevtsov.timetravel.feature.main.domain.mainFunctionalCore

fun screenFunctionalCore(
    state: GameState,
    viewAction: ScreenViewAction,
): GameState {
    return when (viewAction) {
        is ScreenViewAction.Main -> mainFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
        is ScreenViewAction.General -> generalFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
        is ScreenViewAction.Drawer -> drawerFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
    }
}
