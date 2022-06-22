package com.daniil.shevtsov.timetravel.feature.main.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab

data class MainFunctionalCoreState(
    val balanceConfig: BalanceConfig,
    val drawerTabs: List<DrawerTab>,
    val currentScreen: Screen,
)

fun MainFunctionalCoreState.updateGameState(currentState: GameState): GameState = currentState.copy(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,

    currentScreen = currentScreen,
)

fun GameState.toMainState() = MainFunctionalCoreState(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,
    currentScreen = currentScreen,
)
