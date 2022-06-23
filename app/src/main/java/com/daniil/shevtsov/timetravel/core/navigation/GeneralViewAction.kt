package com.daniil.shevtsov.timetravel.core.navigation

sealed class GeneralViewAction {
    data class Open(
        val screen: Screen,
        val shouldReplace: Boolean = false,
        ) : GeneralViewAction()
    object Back : GeneralViewAction()
    object Tick : GeneralViewAction()
}
