package com.daniil.shevtsov.timetravel.feature.coreshell.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.domain.balanceConfig
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot

data class GameState(
    val balanceConfig: BalanceConfig,
    val drawerTabs: List<DrawerTab>,
    val currentScreen: Screen,
    val screenStack: List<Screen>,
    val plot: String,
    val plots: List<Plot>,
)

fun gameState(
    balanceConfig: BalanceConfig = balanceConfig(),
    currentScreen: Screen = Screen.Main,
    screenStack: List<Screen> = emptyList(),
    drawerTabs: List<DrawerTab> = emptyList(),
    plot: String = "",
    plots: List<Plot> = emptyList(),
    ) = GameState(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,
    currentScreen = currentScreen,
    screenStack = screenStack,
    plot = plot,
    plots = plots,
)
