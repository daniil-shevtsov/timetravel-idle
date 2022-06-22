package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTabId

sealed class MainViewAction {
    object Init : MainViewAction()
    data class DrawerTabSwitched(val id : DrawerTabId) : MainViewAction()
}
