package com.daniil.shevtsov.timetravel.core.navigation

import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerViewState

data class ScreenHostViewState(
    val drawerState: DrawerViewState,
    val contentState: ScreenContentViewState,
)
