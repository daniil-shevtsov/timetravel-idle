package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState


sealed class MainViewState {
    object Loading : MainViewState()

    data class Content(
        val plot: PlotViewState,
    ) : MainViewState()
}


