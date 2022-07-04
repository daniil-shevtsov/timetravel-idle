package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.location.presentation.LocationViewState
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState


sealed class MainViewState {
    object Loading : MainViewState()

    data class Content(
        val resources: ResourcesViewState,
        val location:LocationViewState,
        val actions: List<ActionModel>,
        val timeTravel: TimeTravelViewState,
        val plot: PlotViewState,
    ) : MainViewState()
}


