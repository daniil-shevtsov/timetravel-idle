package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab

data class DrawerViewState(
    val tabSelectorState: List<DrawerTab>,
    val drawerContent: DrawerContentViewState,
)
