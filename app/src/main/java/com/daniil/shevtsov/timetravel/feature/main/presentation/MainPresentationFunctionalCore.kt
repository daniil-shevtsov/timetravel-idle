package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.timetravel.feature.main.domain.MainFunctionalCoreState

fun mapMainViewState(
    state: MainFunctionalCoreState
): MainViewState {
    return createMainViewState(state)
}

private fun createMainViewState(state: MainFunctionalCoreState): MainViewState {
    return MainViewState.Success(
        drawerState = DrawerViewState(
            tabSelectorState = state.drawerTabs,
            drawerContent = when (state.drawerTabs.find { it.isSelected }?.id
                ?: DrawerTabId.Debug) {
                DrawerTabId.Debug -> {
                    DrawerContentViewState.Debug(
                        state = DebugViewState,
                    )
                }
            }
        ),
    )
}
