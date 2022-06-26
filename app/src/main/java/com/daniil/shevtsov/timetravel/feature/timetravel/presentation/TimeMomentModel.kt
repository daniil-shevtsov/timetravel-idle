package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import kotlin.time.Duration

data class TimeMomentModel(
    val id: TimeMomentId,
    val time: PassedTime,
    val timelineParent: TimeMomentId?,
)

fun timeMomentModel(
    id: TimeMomentId = TimeMomentId(0L),
    time: PassedTime = PassedTime(Duration.ZERO),
    timelineParent: TimeMomentId? = null,
) = TimeMomentModel(
    id = id,
    time = time,
    timelineParent = timelineParent,
)
