package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugViewState

sealed class DrawerContentViewState {
    data class Debug(val state: DebugViewState): DrawerContentViewState()
}
