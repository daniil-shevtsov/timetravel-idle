package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

data class TimeTravelViewState(
    val moments: List<TimeMomentModel>,
    val lastSelectedMomentId: TimeMomentId?,
)
