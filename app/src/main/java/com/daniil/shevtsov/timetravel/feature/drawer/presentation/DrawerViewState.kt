package com.daniil.shevtsov.timetravel.feature.drawer.presentation

data class DrawerViewState(
    val tabSelectorState: List<DrawerTab>,
    val drawerContent: DrawerContentViewState,
)
