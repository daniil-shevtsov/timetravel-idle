package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.timeline.domain.TimeTravelState
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

data class TimeTravelViewState(
    val moments: List<TimeMomentModel>,
    val isAnimating: Boolean, // Look at mister Bad Practice here
    val lastSelectedMomentId: TimeMomentId?,
    val state: TimeTravelState = TimeTravelState.Stationary,
)
