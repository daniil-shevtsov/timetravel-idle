package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel

data class Moment(
    val id: TimeMomentId,
    val title: String,
    val position: Offset,
)

data class Line(
    val endMomentId: TimeMomentId,
    val start: Offset,
    val end: Offset,
)

data class TimelineSizes(
    val canvasPadding: Float,
    val point: Float,
    val segment: Float,
    val timelineSplitOffset: Offset,
)

data class TimelineViewState(
    val lines: List<Line>,
    val moments: List<Moment>,
)

//TODO: Get rid of side effect
private fun calculateMomentPositionWithSideEffect(
    parentMomentPosition: Offset?,
    parentTimelineId: TimeMomentId?,
    currentMomentTimelineId: TimeMomentId?,
    sizes: TimelineSizes,
): Offset {
    if (parentMomentPosition == null) {
        return Offset(
            x = sizes.canvasPadding + sizes.point / 2,
            y = sizes.canvasPadding + sizes.point / 2,
        )
    }

    if (parentTimelineId != currentMomentTimelineId) {
        return Offset(
            x = parentMomentPosition.x + sizes.point / 2f + sizes.timelineSplitOffset.x,
            y = parentMomentPosition.y + sizes.timelineSplitOffset.y,
        )
    }

    return Offset(
        x = parentMomentPosition.x + (sizes.point / 2f + sizes.segment + sizes.point / 2f),
        y = parentMomentPosition.y
    )
}

fun timelinePresentation(
    allMoments: List<TimeMomentModel>,
    sizes: TimelineSizes,
): TimelineViewState {
    var momentPositions: Map<TimeMomentId, Offset> = mapOf()
    val timelines = allMoments.map { it.timelineParent }.toSet().toList()

    val allTimelines = allMoments.groupBy { it.timelineParent }

    allMoments.forEach { moment ->
        val parentMoment = allMoments.find { moment.momentParents.contains(it.id) }
        val parentTimelineId = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == parentMoment?.id }
        }?.key
        val newPosition = calculateMomentPositionWithSideEffect(
            parentMomentPosition = momentPositions[parentMoment?.id],
            parentTimelineId = parentTimelineId,
            currentMomentTimelineId = moment.timelineParent,
            sizes = sizes,
        )

        momentPositions = momentPositions.toMutableMap().apply {
            put(
                moment.id,
                newPosition,
            )
        }.toMap()
    }

    val lines = mutableListOf<Line>()

    val momentModels = allTimelines.entries.flatMapIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val timelineRootMoment = parentTimeline?.find { it.id == timelineId }

        moments.flatMapIndexed { index, moment ->
            val momentPosition = momentPositions[moments[index].id]!!

            val childMoment = moments.getOrNull(index + 1)
            val childPosition = childMoment?.id?.let {
                momentPositions[it]
            }
            listOfNotNull(
                timelineRootMoment?.let { parentMoment ->
                    val parentPosition = momentPositions[parentMoment.id]
                    parentPosition?.let {
                        Line(
                            endMomentId = moment.id,
                            start = parentPosition,
                            end = momentPosition,
                        )
                    }.takeIf { index == 0 && timelineIndex != 0 }
                },
                childPosition?.let {
                    Line(
                        endMomentId = childMoment.id,
                        start = momentPosition,
                        end = childPosition
                    )
                }
            )
        }.forEach { line -> lines.add(line) }

        moments.mapIndexed { _, moment ->
            val momentPosition = momentPositions[moment.id]!!
            Moment(
                id = moment.id,
                title = moment.time.value.toString(),
                position = momentPosition,
            )
        }
    }

    val newMoment = allMoments.find { it.momentParents.size > 1 }
    if (newMoment != null) {
        val secondParent = momentPositions[newMoment.momentParents[1]]!!
        lines.add(
            Line(
                endMomentId = newMoment.id,
                start = secondParent,
                end = momentPositions[newMoment.id]!!
            )
        )
    }

    return TimelineViewState(
        lines = lines.sortedBy { it.endMomentId.value },
        moments = momentModels.sortedBy { it.id.value },
    )
}
