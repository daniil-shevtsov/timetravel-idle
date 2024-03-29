package com.daniil.shevtsov.timetravel.feature.timetravel.domain

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState

data class TimeMoment(
    val id: TimeMomentId,
    val stateSnapshot: GameState,
    val timelineParentId: TimeMomentId?,
    val parents: List<TimeMomentId>,
)

@JvmInline
value class TimeMomentId(val value: Long)

fun timeMoment(
    id: TimeMomentId = TimeMomentId(0L),
    timelineParentId: TimeMomentId? = null,
    parents: List<TimeMomentId> = emptyList(),
    stateSnapshot: GameState = gameState(),
) = TimeMoment(
    id = id,
    timelineParentId = timelineParentId,
    parents = parents,
    stateSnapshot = stateSnapshot,
)
