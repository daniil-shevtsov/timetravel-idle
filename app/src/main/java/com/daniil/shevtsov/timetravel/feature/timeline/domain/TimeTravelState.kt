package com.daniil.shevtsov.timetravel.feature.timeline.domain

import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

sealed class TimeTravelState {
    object Stationary : TimeTravelState()
    data class Travelling(
        val start: TimeMomentId,
        val destination: TimeMomentId,
    ) : TimeTravelState()
}
