package com.daniil.shevtsov.timetravel.feature.drawer.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugViewAction

sealed class DrawerViewAction {
    data class TabSwitched(val id: DrawerTabId) : DrawerViewAction()
    data class Debug(val action: DebugViewAction) : DrawerViewAction()
}
