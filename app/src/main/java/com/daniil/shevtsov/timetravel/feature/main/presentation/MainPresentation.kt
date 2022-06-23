package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.plot.domain.Choice
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import kotlin.time.DurationUnit

fun mapMainViewState(
    state: GameState
): MainViewState {
    return createMainViewState(state)
}

private fun createMainViewState(state: GameState): MainViewState {
    return MainViewState.Content(
        plot = state.plot.toViewState(),
        resources = ResourcesViewState(passedTime = state.passedTime.toModel())
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
    text = value.toString(DurationUnit.SECONDS, decimals = 2)
)
