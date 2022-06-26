package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import com.daniil.shevtsov.timetravel.feature.main.view.MomentPosition
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel

data class Moment(
    val title: String,
    val position: Offset,
)

data class TimelineSizes(
    val canvasPadding: Float,
    val point: Float,
    val segment: Float,
)

fun timelinePresentation(
    allTimelines: Map<TimeMomentId?, List<TimeMomentModel>>,
    sizes: TimelineSizes,
): List<Moment> {
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
            horizontalPadding + index * sizes.segment + sizes.point / 2
            val circlePosition =
                horizontalPadding + index * sizes.segment + sizes.point / 2
            momentPositions = momentPositions.toMutableMap().apply {
                put(moment.id, MomentPosition(Offset(x = circlePosition, y = verticalPadding)))
            }.toMap()
        }
    }

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

        moments.map { moment ->
            Moment(
                title = moment.time.value.toString(),
                position = Offset(momentPositions[moment.id]?.position?.x ?: 0f, verticalPadding)
            )
        }
    }

    return momentModels
}
