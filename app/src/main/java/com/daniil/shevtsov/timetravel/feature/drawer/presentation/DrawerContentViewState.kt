package com.daniil.shevtsov.timetravel.feature.drawer.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.timetravel.feature.info.presentation.InfoViewState

sealed class DrawerContentViewState {
    data class Info(val state: InfoViewState) : DrawerContentViewState()
    data class Debug(val state: DebugViewState) : DrawerContentViewState()
}
