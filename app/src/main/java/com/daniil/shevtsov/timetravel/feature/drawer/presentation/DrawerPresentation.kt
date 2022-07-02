package com.daniil.shevtsov.timetravel.feature.drawer.presentation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.debug.presentation.debugViewState
import com.daniil.shevtsov.timetravel.feature.info.presentation.infoPresentation

fun drawerPresentation(
    state: GameState
): DrawerViewState {

    return DrawerViewState(
        tabSelectorState = state.drawerTabs,
        drawerContent = when (state.drawerTabs.find { it.isSelected }?.id
            ?: DrawerTabId.Debug) {
            DrawerTabId.Info -> {
                DrawerContentViewState.Info(infoPresentation(state))
            }
            DrawerTabId.Debug -> {
                DrawerContentViewState.Debug(debugViewState())
            }
        }
    )
}
