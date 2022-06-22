package com.daniil.shevtsov.timetravel.feature.drawer.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.timetravel.feature.main.domain.handleDrawerTabSwitched

fun drawerFunctionalCore(
    state: GameState,
    viewAction: DrawerViewAction
): GameState {
    return when (viewAction) {
        is DrawerViewAction.TabSwitched -> handleDrawerTabSwitched(
            state = state,
            viewAction = viewAction,
        )
        is DrawerViewAction.Debug -> state
    }
}
