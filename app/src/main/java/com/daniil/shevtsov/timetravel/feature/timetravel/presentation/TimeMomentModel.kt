package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

data class TimeMomentModel(
    val id: TimeMomentId,
    val time: PassedTime,
    val timelineParent: TimeMomentId? = null,
)
