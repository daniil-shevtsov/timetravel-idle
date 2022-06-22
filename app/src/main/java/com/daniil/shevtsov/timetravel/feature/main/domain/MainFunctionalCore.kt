package com.daniil.shevtsov.timetravel.feature.main.domain

import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction

fun mainFunctionalCore(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction,
): MainFunctionalCoreState {
    val newState = when (viewAction) {
        is MainViewAction.DrawerTabSwitched -> handleDrawerTabSwitched(
            state = state,
            viewAction = viewAction,
        )
        MainViewAction.Init -> state
    }
    return newState
}

fun handleDrawerTabSwitched(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.DrawerTabSwitched
): MainFunctionalCoreState {
    return state.copy(
        drawerTabs = state.drawerTabs.map { tab ->
            tab.copy(isSelected = tab.id == viewAction.id)
        }
    )
}
