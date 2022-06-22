package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.debugViewState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab

fun mainViewState(
    drawerState: DrawerViewState = drawerViewState(),
) = MainViewState.Success(
    drawerState = drawerState,
)

fun drawerViewState(
    tabSelectorState: List<DrawerTab> = emptyList(),
    drawerContent: DrawerContentViewState = drawerDebugContent(),
) = DrawerViewState(
    tabSelectorState = tabSelectorState,
    drawerContent = drawerContent,
)

fun drawerDebugContent(
) = DrawerContentViewState.Debug(
    state = debugViewState(
    ),
)
