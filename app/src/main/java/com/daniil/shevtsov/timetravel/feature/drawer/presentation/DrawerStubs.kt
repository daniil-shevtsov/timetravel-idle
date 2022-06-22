package com.daniil.shevtsov.timetravel.feature.drawer.presentation

import com.daniil.shevtsov.timetravel.feature.debug.presentation.debugViewState

fun drawerTab(
    id: DrawerTabId = DrawerTabId.Debug,
    title: String = "",
    isSelected: Boolean = false,
) = DrawerTab(
    id = id,
    title = title,
    isSelected = isSelected,
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
    state = debugViewState(),
)
