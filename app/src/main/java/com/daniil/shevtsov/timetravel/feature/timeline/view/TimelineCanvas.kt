package com.daniil.shevtsov.timetravel.feature.timeline.view

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.main.view.distanceTo
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.TimelineSizes
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.timelinePresentation
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun TimelineCanvasPreview() {
    TimelineCanvas(state = timeTravelStatePreviewData(), onViewAction = {})
}

@Composable
fun TimelineCanvas(
    state: TimeTravelViewState,
    onViewAction: (MainViewAction) -> Unit
) {
    val allTimelines = state.moments
        .groupBy { it.timelineParent }

    val pointSize = with(LocalDensity.current) { 40.dp.toPx() }
    val lineHeight = with(LocalDensity.current) { 8.dp.toPx() }
    val segmentLength = with(LocalDensity.current) { 20.dp.toPx() }
    val timelineOffset = with(LocalDensity.current) {
        Offset(
            x = 10.dp.toPx(),
            y = pointSize + 10,
        )
    }
    val canvasPadding = with(LocalDensity.current) { 25.dp.toPx() }
    val textSize = with(LocalDensity.current) { 12.sp.toPx() }

    val lineColor = AppTheme.colors.textLight
    val pointColor = AppTheme.colors.background
    val selectedPointColor = AppTheme.colors.backgroundLight
    val textColor = AppTheme.colors.textLight

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.textSize = textSize
        color = textColor.toArgb()
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textAlign = android.graphics.Paint.Align.CENTER
    }
    val sizes = TimelineSizes(
        canvasPadding = canvasPadding,
        point = pointSize,
        segment = segmentLength,
        timelineSplitOffset = timelineOffset,
    )
    val timelineState = timelinePresentation(
        allMoments = allTimelines.values.flatten(),
        sizes = sizes
    )

    val furthestMomentX = with(LocalDensity.current) {
        timelineState.moments.maxByOrNull { it.position.x }?.position?.x?.let {
            it + canvasPadding + pointSize / 2
        }?.toDp()
    }

    val width = when {
        furthestMomentX == null || furthestMomentX < 500.dp -> 500.dp
        else -> furthestMomentX
    }


    Canvas(
        modifier = Modifier
            .background(AppTheme.colors.backgroundDarkest)
            .horizontalScroll(rememberScrollState())
            .width(width)
            .height(180.dp) //TODO: Add vertical scroll
            .pointerInput(timelineState.moments) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val nearestMoment =
                            timelineState.moments.minByOrNull { model ->
                                tapOffset.distanceTo(model.position)
                            }
                        if (nearestMoment != null
                            && nearestMoment.position.distanceTo(tapOffset) <= pointSize
                        ) {
                            onViewAction(MainViewAction.TravelBackToMoment(nearestMoment.id))
                        }
                    })
            }) {

        timelineState.lines.forEach { line ->
            drawLine(
                color = lineColor,
                strokeWidth = lineHeight,
                start = line.start,
                end = line.end,
            )
        }

        timelineState.moments.forEach { model ->
            drawCircle(
                color = pointColor,
                radius = pointSize / 2,
                center = model.position
            )
            if (model.id == state.lastSelectedMomentId) {
                drawCircle(
                    color = selectedPointColor,
                    radius = pointSize * 0.5f,
                    center = model.position
                )
                drawCircle(
                    color = pointColor,
                    radius = pointSize * 0.40f,
                    center = model.position
                )
            }
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    model.title,
                    model.position.x,
                    model.position.y + textSize / 2,
                    textPaint
                )
            }
        }
    }
}

fun timeTravelStatePreviewData(): TimeTravelViewState = TimeTravelViewState(
    moments = listOf(
        timeMomentModel(
            id = TimeMomentId(1L),
            time = PassedTime(4L.seconds),
        ),
        timeMomentModel(
            id = TimeMomentId(2L),
            time = PassedTime(8L.seconds),
            momentParents = listOf(TimeMomentId(1L)),
        ),
        timeMomentModel(
            id = TimeMomentId(3L),
            time = PassedTime(10L.seconds),
            momentParents = listOf(TimeMomentId(2L)),
        ),
        timeMomentModel(
            id = TimeMomentId(4L),
            time = PassedTime(11L.seconds),
            momentParents = listOf(TimeMomentId(3L)),
        ),
        timeMomentModel(
            id = TimeMomentId(5L),
            time = PassedTime(12L.seconds),
            momentParents = listOf(
                TimeMomentId(4),
                TimeMomentId(10),
            )
        ),
        timeMomentModel(
            id = TimeMomentId(6L),
            time = PassedTime(13L.seconds),
            momentParents = listOf(TimeMomentId(5L)),
        ),
        timeMomentModel(
            id = TimeMomentId(7L),
            time = PassedTime(15L.seconds),
            timelineParent = TimeMomentId(2L),
            momentParents = listOf(TimeMomentId(2L)),
        ),
        timeMomentModel(
            id = TimeMomentId(8L),
            time = PassedTime(16L.seconds),
            timelineParent = TimeMomentId(2L),
            momentParents = listOf(TimeMomentId(7L)),
        ),
        timeMomentModel(
            id = TimeMomentId(9L),
            time = PassedTime(17L.seconds),
            timelineParent = TimeMomentId(7L),
            momentParents = listOf(TimeMomentId(7L)),
        ),
        timeMomentModel(
            id = TimeMomentId(10L),
            time = PassedTime(18L.seconds),
            timelineParent = TimeMomentId(7L),
            momentParents = listOf(TimeMomentId(9L)),
        ),
    ),
    lastSelectedMomentId = TimeMomentId(1L),
)
