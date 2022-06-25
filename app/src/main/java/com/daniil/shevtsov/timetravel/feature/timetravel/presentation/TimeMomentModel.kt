package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

data class TimeMomentModel(
    val id: TimeMomentId,
    val time: PassedTime,
    val timelineParent: TimeMomentId?,
)

fun timeMomentModel(
    id: TimeMomentId,
    time: PassedTime,
    timelineParent: TimeMomentId? = null,
) = TimeMomentModel(
    id = id,
    time = time,
    timelineParent = timelineParent,
)
