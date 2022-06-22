package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.plot.domain.Choice
import com.daniil.shevtsov.timetravel.feature.plot.domain.Plot
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState

fun mapMainViewState(
    state: GameState
): MainViewState {
    return createMainViewState(state)
}

private fun createMainViewState(state: GameState): MainViewState {
    return MainViewState.Content(
        plot = state.plot.toViewState(),
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
