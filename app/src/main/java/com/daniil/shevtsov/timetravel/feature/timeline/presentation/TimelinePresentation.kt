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
    val timelineOffset: Float,
)

data class TimelineViewState(
    val lines: List<Line>,
    val moments: List<Moment>,
)

private fun calculateMomentPositionWithSideEffect(
    parentCenterX: Float,
    timelineIndex: Int,
    momentIndex: Int,
    sizes: TimelineSizes,
): Offset {
    val start = when (timelineIndex) {
        0 -> sizes.canvasPadding + sizes.point / 2
        else -> parentCenterX + sizes.timelineOffset
    }

    val verticalPadding = sizes.canvasPadding + timelineIndex * (sizes.point + 10)
    val circlePosition = start + momentIndex * sizes.segment
    return Offset(
        x = circlePosition,
        y = verticalPadding + sizes.point / 2
    )
}

fun calculateMomentPositionPure(
    timelineIndex: Int,
    momentIndex: Int,
    sizes: TimelineSizes,
): Offset {

    val start = when (timelineIndex) {
        0 -> sizes.canvasPadding + sizes.point / 2
        else -> sizes.canvasPadding + sizes.point / 2 + sizes.segment * momentIndex + sizes.timelineOffset
    }

    val verticalPadding = sizes.canvasPadding + timelineIndex * (sizes.point + 10)
    val circlePosition = start + momentIndex * sizes.segment

    return Offset(
        x = circlePosition,
        y = verticalPadding + sizes.point / 2
    )
}

fun timelinePresentation(
    allTimelines: Map<TimeMomentId?, List<TimeMomentModel>>,
    sizes: TimelineSizes,
): TimelineViewState {
    var momentPositions: Map<TimeMomentId, Offset> = mapOf()
    allTimelines.entries.forEachIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val parentCenterX = parentTimeline
            ?.find { it.id == timelineId }
            ?.let { parentMoment ->
                momentPositions[parentMoment.id]?.x
            } ?: 0f

        moments.forEachIndexed { index, moment ->
            val position = if(timelineIndex == 0) {
                calculateMomentPositionPure(
                    timelineIndex = timelineIndex,
                    momentIndex = index,
                    sizes = sizes,
                )
            } else {
                calculateMomentPositionWithSideEffect(
                    parentCenterX = parentCenterX,
                    timelineIndex = timelineIndex,
                    momentIndex = index,
                    sizes = sizes,
                )
            }

            momentPositions = momentPositions.toMutableMap().apply {
                put(
                    moment.id,
                    position,
                )
            }.toMap()
        }
    }

    val lines = mutableListOf<Line>()

    val momentModels = allTimelines.entries.flatMapIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val parentMoment = parentTimeline?.find { it.id == timelineId }

        moments.flatMapIndexed { index, moment ->
            val momentPosition = momentPositions[moments[index].id]!!

            val childMoment = moments.getOrNull(index + 1)
            val childPosition = childMoment?.id?.let {
                momentPositions[it]
            }
            listOfNotNull(
                parentMoment?.let { parentMoment ->
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

        moments.mapIndexed { index, moment ->
            val momentPosition = momentPositions[moment.id]!!
            Moment(
                id = moment.id,
                title = moment.time.value.toString(),
                position = momentPosition,
            )
        }
    }

    return TimelineViewState(
        lines = lines,
        moments = momentModels,
    )
}
