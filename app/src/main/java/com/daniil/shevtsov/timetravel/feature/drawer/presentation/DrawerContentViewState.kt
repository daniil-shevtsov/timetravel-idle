package com.daniil.shevtsov.timetravel.feature.drawer.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugViewState

sealed class DrawerContentViewState {
    data class Debug(val state: DebugViewState): DrawerContentViewState()
}
