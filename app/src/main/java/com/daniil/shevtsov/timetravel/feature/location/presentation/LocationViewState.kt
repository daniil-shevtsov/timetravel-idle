package com.daniil.shevtsov.timetravel.feature.location.presentation

import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorViewState

data class LocationViewState(
    val selector: SelectorViewState,
    val description: String,
)
