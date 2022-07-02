package com.daniil.shevtsov.timetravel.feature.main.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

fun mainFunctionalCore(
    state: GameState,
    viewAction: MainViewAction,
): GameState {
    val newState = when (viewAction) {
        is MainViewAction.Init -> state
        is MainViewAction.SelectChoice -> selectChoice(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.SelectAction -> selectAction(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.TravelBackToMoment -> travelInTime(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.RegisterTimePoint -> registerTimePoint(
            state = state,
            viewAction = viewAction,
        )
    }
    return newState
}

fun selectChoice(
    state: GameState,
    viewAction: MainViewAction.SelectChoice
): GameState {
    val selectedChoice =
        state.plot.choices.find { choice -> choice.id == viewAction.id } ?: return state
    val newPlot =
        state.plots.find { plot -> plot.id == selectedChoice.destinationPlotId } ?: return state
    val newTags = (state.presentTags + newPlot.tagsToAdd).toSet().toList()
    return state.copy(
        plot = newPlot,
        presentTags = newTags,
    )
}

fun selectAction(
    state: GameState,
    viewAction: MainViewAction.SelectAction
): GameState {
    val selectedAction = state.actions.find { action -> action.id == viewAction.id } ?: return state
    val newResources = state.resources.map { resource ->
        val resourceChange = selectedAction.resourceChanges[resource.id]
        if (resourceChange != null) {
            resource.copy(
                value = resource.value + resourceChange
            )
        } else {
            resource
        }
    }
    return state.copy(resources = newResources)
}

fun travelInTime(state: GameState, viewAction: MainViewAction.TravelBackToMoment): GameState {
    val selectedMoment =
        state.timeMoments.find { moment -> moment.id == viewAction.id } ?: return state
    return selectedMoment.stateSnapshot.copy(
        timeMoments = state.timeMoments,
        currentMomentId = selectedMoment.id,
    )
}

fun registerTimePoint(state: GameState, viewAction: MainViewAction.RegisterTimePoint): GameState {
    val currentTimeMoment = state.timeMoments.find { it.id == state.currentMomentId }
    val isTheFirstMoment = state.currentMomentId == null
    val isLastInTimeline =
        state.timeMoments.lastOrNull { it.timelineParentId == currentTimeMoment?.timelineParentId } == currentTimeMoment
    val isSomething =
        state.timeMoments.size - 1 != state.timeMoments.indexOfFirst { it.id == state.currentMomentId }

    val newTimelineParentId = when {
        isTheFirstMoment -> null
        isLastInTimeline -> currentTimeMoment?.timelineParentId
        isSomething -> state.currentMomentId
        else -> null
    }
    val newParents = when {
        isTheFirstMoment -> emptyList()
        isLastInTimeline -> listOfNotNull(state.timeMoments.lastOrNull { it.timelineParentId == currentTimeMoment?.timelineParentId }?.id)
        isSomething -> listOfNotNull(state.currentMomentId)
        else -> emptyList()
    }
    val newMoment = TimeMoment(
        id = TimeMomentId(
            (state.timeMoments.lastOrNull()?.id?.value ?: 0L) + 1L
        ),
        timelineParentId = newTimelineParentId,
        parents = newParents,
        stateSnapshot = state,
    )

    return state.copy(
        timeMoments = state.timeMoments + listOf(
            newMoment
        ),
        currentMomentId = newMoment.id,
    )
}

fun handleDrawerTabSwitched(
    state: GameState,
    viewAction: DrawerViewAction.TabSwitched
): GameState {
    return state.copy(
        drawerTabs = state.drawerTabs.map { tab ->
            tab.copy(isSelected = tab.id == viewAction.id)
        }
    )
}
