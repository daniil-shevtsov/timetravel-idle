package com.daniil.shevtsov.timetravel.feature.coreshell.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.domain.SelectorExpandedStates
import com.daniil.shevtsov.timetravel.core.domain.balanceConfig
import com.daniil.shevtsov.timetravel.core.domain.selectorExpandedStates
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.actions.domain.Action
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.timetravel.feature.location.domain.Location
import com.daniil.shevtsov.timetravel.feature.location.domain.LocationId
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot
import com.daniil.shevtsov.timetravel.feature.plot.domain.plot
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.StoredResource
import com.daniil.shevtsov.timetravel.feature.tags.domain.Tag
import com.daniil.shevtsov.timetravel.feature.tags.domain.TagId
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timeline.domain.TimeTravelState
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
    val storedResources: List<StoredResource>,
    val actions: List<Action>,
    val allTags: List<Tag>,
    val presentTags: List<TagId>,
    val timeMoments: List<TimeMoment>,
    val allLocations: List<Location>,
    val currentLocationId: LocationId,
    val selectorExpandedStates: SelectorExpandedStates,
    val currentMomentId: TimeMomentId? = null,
    val isAnimating: Boolean = false,
    val timeTravel: TimeTravelState = TimeTravelState.Stationary,
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
    storedResources: List<StoredResource> = emptyList(),
    allTags: List<Tag> = emptyList(),
    presentTags: List<TagId> = emptyList(),
    actions: List<Action> = emptyList(),
    timeMoments: List<TimeMoment> = emptyList(),
    allLocations: List<Location> = emptyList(),
    currentLocationId: LocationId = LocationId(0L),
    lastTimeMomentId: TimeMomentId? = null,
    selectorExpandedStates: SelectorExpandedStates = selectorExpandedStates(),
) = GameState(
    balanceConfig = balanceConfig,
    drawerTabs = drawerTabs,
    currentScreen = currentScreen,
    screenStack = screenStack,
    plot = plot,
    plots = plots,
    passedTime = passedTime,
    resources = resources,
    storedResources = storedResources,
    actions = actions,
    allTags = allTags,
    presentTags = presentTags,
    timeMoments = timeMoments,
    allLocations = allLocations,
    currentLocationId = currentLocationId,
    currentMomentId = lastTimeMomentId,
    selectorExpandedStates = selectorExpandedStates,
)
