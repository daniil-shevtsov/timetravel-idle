package com.daniil.shevtsov.timetravel.feature.timetravel.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState

data class TimeMoment(
    val id: TimeMomentId,
    val stateSnapshot: GameState,
)

@JvmInline
value class TimeMomentId(val value: Long)

fun timeMoment(
    id: TimeMomentId = TimeMomentId(0L),
    stateSnapshot: GameState,
) = TimeMoment(
    id = id,
    stateSnapshot = stateSnapshot,
)
