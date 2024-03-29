package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction

sealed class ScreenViewAction {
    data class Drawer(val action: DrawerViewAction) : ScreenViewAction()
    data class General(val action: GeneralViewAction) : ScreenViewAction()
    data class Main(val action: MainViewAction) : ScreenViewAction()
}
