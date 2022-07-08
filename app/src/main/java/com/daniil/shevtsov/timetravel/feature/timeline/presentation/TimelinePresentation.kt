package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import kotlin.time.DurationUnit

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

    allMoments.forEach { moment ->
        moment.momentParents.forEach { parentMoment ->
            val momentPosition = momentPositions[moment.id]!!
            val parentPosition = momentPositions[parentMoment]
            if (parentPosition != null) {
                lines.add(
                    Line(
                        endMomentId = moment.id,
                        start = parentPosition,
                        end = momentPosition,
                    )
                )
            }
        }
    }

    val momentModels = allMoments.map { moment ->
        val momentPosition = momentPositions[moment.id]!!

        val timeString: String = with(moment.time.value) {
            when {
                inWholeMilliseconds < 100 -> toString(
                    unit = DurationUnit.MILLISECONDS,
                    decimals = 1,
                )
                inWholeSeconds < 100 -> toString(
                    unit = DurationUnit.SECONDS,
                    decimals = 1,
                )
                inWholeMinutes < 100 -> toString(
                    unit = DurationUnit.MINUTES,
                    decimals = 1,
                )
                inWholeHours < 100 -> toString(
                    unit = DurationUnit.HOURS,
                    decimals = 1,
                )
                inWholeDays < 100 -> toString(
                    unit = DurationUnit.DAYS,
                    decimals = 1,
                )
                else -> toString(
                    unit = DurationUnit.DAYS,
                    decimals = 1,
                )
            }
        }.let { timeString ->
            val indexOfFirstLetter = if (timeString.any { it.isLetter() }) {
                timeString
                    .indexOfFirst { char -> char.isLetter() }
            } else {
                return@let timeString
            }


            timeString.substring(0, indexOfFirstLetter) + "\n" + timeString.substring(
                indexOfFirstLetter,
                timeString.length
            )
        }

        Moment(
            id = moment.id,
            title = timeString,
            position = momentPosition,
        )
    }

    return TimelineViewState(
        lines = lines.sortedBy { it.endMomentId.value },
        moments = momentModels.sortedBy { it.id.value },
    )
}
