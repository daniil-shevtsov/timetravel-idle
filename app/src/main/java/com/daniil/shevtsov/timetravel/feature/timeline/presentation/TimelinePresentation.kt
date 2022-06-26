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
    val start: Offset,
    val end: Offset,
)

data class TimelineSizes(
    val canvasPadding: Float,
    val point: Float,
    val segment: Float,
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
        val splitPadding = parentTimeline
            ?.find { it.id == timelineId }
            ?.let { parentMoment ->
                momentPositions[parentMoment.id]?.position?.x
            } ?: 0f
        val horizontalPadding = sizes.canvasPadding + splitPadding
        val verticalPadding = sizes.canvasPadding + timelineIndex * (sizes.point + 10)
        moments.forEachIndexed { index, moment ->
            val circlePosition = horizontalPadding + index * sizes.segment + sizes.point / 2
            momentPositions = momentPositions.toMutableMap().apply {
                put(moment.id, MomentPosition(Offset(x = circlePosition, y = verticalPadding)))
            }.toMap()
        }
    }

    val lines = mutableListOf<Line>()

    val momentModels = allTimelines.entries.flatMapIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val parentMoment = parentTimeline?.find { it.id == timelineId }
        val splitPadding = parentMoment?.let {
            momentPositions[it.id]?.position?.x
        } ?: 0f
        val horizontalPadding = sizes.canvasPadding + splitPadding
        val verticalPadding = sizes.canvasPadding + timelineIndex * (sizes.point + 10)

        lines.add(
            Line(
                start = Offset(
                    horizontalPadding,
                    verticalPadding
                ),
                end = Offset(
                    horizontalPadding + moments.size * sizes.segment,
                    verticalPadding
                )
            )
        )
        moments.mapIndexed { index, moment ->
            val momentPosition = Offset(momentPositions[moment.id]?.position?.x ?: 0f, verticalPadding)
            if (index == 0 && parentMoment != null) {
                val parentPosition = momentPositions[parentMoment.id]?.position!!
                lines.add(
                    Line(
                        start = parentPosition,
                        end = momentPosition,
                    )
                )
            }

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
