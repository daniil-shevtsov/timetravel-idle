package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorId
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorModel
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorViewState
import com.daniil.shevtsov.timetravel.feature.actions.domain.Action
import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.location.presentation.LocationViewState
import com.daniil.shevtsov.timetravel.feature.plot.domain.Choice
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import kotlin.time.DurationUnit

fun mapMainViewState(
    state: GameState
): MainViewState {
    return MainViewState.Content(
        plot = state.plot.toViewState(),
        resources = ResourcesViewState(
            passedTime = state.passedTime.toModel(),
            resources = state.resources
                .filter { resource -> resource.value > 0f }
                .map { it.toModel() }
        ),
        actions = state.actions
            .filter { action ->
                action.resourceChanges.all { (resourceId, change) ->
                    val resourceToChange = state.resources.find { it.id == resourceId }?.value ?: 0f
                    val resourceAfterChange = resourceToChange + change
                    resourceAfterChange >= 0f
                }
            }
            .filter { action -> state.presentTags.containsAll(action.requiredTags) }
            .map { it.toModel() },
        timeTravel = TimeTravelViewState(
            moments = state.timeMoments.mapIndexed { _, timeMoment ->
                timeMoment.toModel(
                    momentParents = timeMoment.parents
                )
            },
            lastSelectedMomentId = state.currentMomentId
        ),
        location = LocationViewState(
            selector = SelectorViewState(
                items = state.allLocations.map {
                    SelectorModel(
                        id = SelectorId(it.id.raw),
                        title = it.title,
                    )
                },
                selectedItem = (state.allLocations.find { it.id == state.currentLocationId }
                    ?: state.allLocations.firstOrNull())
                    ?.let {
                        SelectorModel(
                            id = SelectorId(it.id.raw),
                            title = it.title,
                        )
                    },
                isExpanded = true,
            ),
            description = state.allLocations.find { it.id == state.currentLocationId }?.description.orEmpty(),
        )
    )
}

private fun Plot.toViewState() = PlotViewState(
    text = text,
    choices = choices.map { it.toModel() },
)

private fun Choice.toModel() = ChoiceModel(
    id = id,
    text = text,
)

private fun PassedTime.toModel() = ResourceModel(
    id = ResourceId.Time,
    title = "Passed Time",
    text = value.toString(DurationUnit.SECONDS, decimals = 2)
)

private fun Resource.toModel() = ResourceModel(
    id = id,
    title = name,
    text = value.toString(),
)

private fun Action.toModel() = ActionModel(
    id = id,
    title = title,
)

private fun TimeMoment.toModel(
    momentParents: List<TimeMomentId>,
) = TimeMomentModel(
    id = id,
    time = stateSnapshot.passedTime,
    timelineParent = timelineParentId,
    momentParents = momentParents,
)
