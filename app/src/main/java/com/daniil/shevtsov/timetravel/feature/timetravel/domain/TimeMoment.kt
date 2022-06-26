package com.daniil.shevtsov.timetravel.feature.timetravel.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState

data class TimeMoment(
    val id: TimeMomentId,
    val stateSnapshot: GameState,
    val timelineParentId: TimeMomentId?,
)

@JvmInline
value class TimeMomentId(val value: Long)

@JvmInline
value class TimeLineId(val value: Long)

fun timeMoment(
    id: TimeMomentId = TimeMomentId(0L),
    timelineParentId: TimeMomentId? = null,
    stateSnapshot: GameState,
) = TimeMoment(
    id = id,
    timelineParentId = timelineParentId,
    stateSnapshot = stateSnapshot,
)
