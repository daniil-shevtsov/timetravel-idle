package com.daniil.shevtsov.timetravel.feature.coreshell.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.domain.balanceConfig
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.actions.domain.Action
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot
import com.daniil.shevtsov.timetravel.feature.plot.domain.plot
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import kotlin.time.Duration

data class GameState(
    val balanceConfig: BalanceConfig,
    val drawerTabs: List<DrawerTab>,
    val currentScreen: Screen,
    val screenStack: List<Screen>,
    val plot: Plot,
    val plots: List<Plot>,
    val passedTime: PassedTime,
    val resources: List<Resource>,
    val actions: List<Action>,
    val timeMoments: List<TimeMoment>,
    val currentMomentId: TimeMomentId? = null,
)

fun gameState(
    balanceConfig: BalanceConfig = balanceConfig(),
    currentScreen: Screen = Screen.Main,
    screenStack: List<Screen> = emptyList(),
    drawerTabs: List<DrawerTab> = emptyList(),
    plot: Plot = plot(),
    plots: List<Plot> = emptyList(),
    passedTime: PassedTime = PassedTime(Duration.ZERO),
    resources: List<Resource> = emptyList(),
    actions: List<Action> = emptyList(),
    timeMoments: List<TimeMoment> = emptyList(),
    lastTimeMomentId: TimeMomentId? = null,
) = GameState(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,
    currentScreen = currentScreen,
    screenStack = screenStack,
    plot = plot,
    plots = plots,
    passedTime = passedTime,
    resources = resources,
    actions = actions,
    timeMoments = timeMoments,
    currentMomentId = lastTimeMomentId,
)
