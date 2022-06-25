package com.daniil.shevtsov.timetravel.feature.timetravel.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState

data class TimeMoment(
    val id: TimeMomentId,
    val timeLineId: TimeLineId,
    val stateSnapshot: GameState,
)

@JvmInline
value class TimeMomentId(val value: Long)

@JvmInline
value class TimeLineId(val value: Long)

fun timeMoment(
    id: TimeMomentId = TimeMomentId(0L),
    timeLineId: TimeLineId = TimeLineId(0L),
    stateSnapshot: GameState,
) = TimeMoment(
    id = id,
    timeLineId = timeLineId,
    stateSnapshot = stateSnapshot,
)
