package com.daniil.shevtsov.timetravel.feature.main.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction

fun mainFunctionalCore(
    state: GameState,
    viewAction: MainViewAction,
): GameState {
    val newState = when (viewAction) {
        is MainViewAction.Init -> state
        is MainViewAction.SelectChoice -> selectChoice(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.SelectAction -> selectAction(
            state = state,
            viewAction = viewAction,
        )
    }
    return newState
}

fun selectChoice(
    state: GameState,
    viewAction: MainViewAction.SelectChoice
): GameState {
    val selectedChoice =
        state.plot.choices.find { choice -> choice.id == viewAction.id } ?: return state
    val newPlot =
        state.plots.find { plot -> plot.id == selectedChoice.destinationPlotId } ?: return state
    return state.copy(
        plot = newPlot
    )
}

fun selectAction(
    state: GameState,
    viewAction: MainViewAction.SelectAction
): GameState {
    return state
}

fun handleDrawerTabSwitched(
    state: GameState,
    viewAction: DrawerViewAction.TabSwitched
): GameState {
    return state.copy(
        drawerTabs = state.drawerTabs.map { tab ->
            tab.copy(isSelected = tab.id == viewAction.id)
        }
    )
}
