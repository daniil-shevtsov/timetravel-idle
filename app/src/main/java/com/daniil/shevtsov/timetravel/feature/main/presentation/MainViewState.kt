package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState


sealed class MainViewState {
    object Loading : MainViewState()

    data class Content(
        val plot: PlotViewState,
        val resources: ResourcesViewState,
        val actions: List<ActionModel>,
    ) : MainViewState()
}


