package com.daniil.shevtsov.timetravel.feature.main.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.domain.balanceConfig
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab

fun mainFunctionalCoreState(
    balanceConfig: BalanceConfig = balanceConfig(),
    drawerTabs: List<DrawerTab> = emptyList(),
    currentScreen: Screen = Screen.Main,
) = MainFunctionalCoreState(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,
    currentScreen = currentScreen,
)
