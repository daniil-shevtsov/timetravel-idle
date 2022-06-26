package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import com.daniil.shevtsov.timetravel.feature.main.view.MomentPosition
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

fun timelinePresentation(
    allTimelines: Map<TimeMomentId?, List<TimeMomentModel>>,
    sizes: TimelineSizes,
): TimelineViewState {
    var momentPositions: Map<TimeMomentId, MomentPosition> = mapOf()
    allTimelines.entries.forEachIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val parentCenter = parentTimeline
            ?.find { it.id == timelineId }
            ?.let { parentMoment ->
                momentPositions[parentMoment.id]?.position?.x
            } ?: 0f
        val verticalPadding = sizes.canvasPadding + timelineIndex * (sizes.point + 10)
        moments.forEachIndexed { index, moment ->
            val startPadding = if (timelineIndex == 0) {
                sizes.canvasPadding + sizes.point / 2
            } else {
                0f
            }
            val offset = if (index == 0) {
                sizes.timelineOffset
            } else {
                0f
            }

            val start = when {
                timelineIndex == 0 -> sizes.canvasPadding + sizes.point / 2
                else -> parentCenter + sizes.timelineOffset
            }

            val circlePosition = start + index * sizes.segment
            momentPositions = momentPositions.toMutableMap().apply {
                put(
                    moment.id,
                    MomentPosition(
                        Offset(
                            x = circlePosition,
                            y = verticalPadding + sizes.point / 2
                        )
                    )
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
            val momentPosition = momentPositions[moments[index].id]?.position!!

            val childMoment = moments.getOrNull(index + 1)
            val childPosition = childMoment?.id?.let {
                momentPositions[it]?.position
            }
            listOfNotNull(
                parentMoment?.let { parentMoment ->
                    val parentPosition = momentPositions[parentMoment.id]?.position
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
            val momentPosition = momentPositions[moment.id]?.position!!
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
