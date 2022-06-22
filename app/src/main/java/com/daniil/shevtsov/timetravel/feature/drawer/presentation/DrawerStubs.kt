package com.daniil.shevtsov.timetravel.feature.drawer.presentation

fun drawerTab(
    id: DrawerTabId = DrawerTabId.Debug,
    title: String = "",
    isSelected: Boolean = false,
) = DrawerTab(
    id = id,
    title = title,
    isSelected = isSelected,
)
