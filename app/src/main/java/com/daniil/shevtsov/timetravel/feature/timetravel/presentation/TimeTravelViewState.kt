package com.daniil.shevtsov.timetravel.feature.timetravel.presentation

import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

data class TimeTravelViewState(
    val moments: List<TimeMomentModel>,
    val timelineGraph: List<TimelineGraphNode>,
    val lastSelectedMomentId: TimeMomentId?,
)

data class TimelineGraphNode(
    val id: TimeMomentId,
    val parents: List<TimeMomentId>,
)

fun timelineGraphNode(
    id: TimeMomentId = TimeMomentId(0),
    parents: List<TimeMomentId> = emptyList(),
) = TimelineGraphNode(
    id = id,
    parents = parents,
)
