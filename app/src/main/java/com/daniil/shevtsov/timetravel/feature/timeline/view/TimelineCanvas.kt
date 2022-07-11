package com.daniil.shevtsov.timetravel.feature.timeline.view

import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
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
import com.daniil.shevtsov.timetravel.feature.timeline.domain.TimeTravelState
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.TimelineSizes
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.timelinePresentation
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun TimelineCanvasPreview() {
    TimelineCanvas(state = timeTravelStatePreviewData(), onViewAction = {})
}

@Preview(widthDp = 500)
@Composable
fun TimelineTextPreview() {
    TimelineCanvas(state = timeTravelStatePreviewData(
        moments = listOf(
            timeMomentModel(id = TimeMomentId(0L), time = PassedTime(5.milliseconds)),
            timeMomentModel(id = TimeMomentId(1L), time = PassedTime(20.milliseconds)),
            timeMomentModel(id = TimeMomentId(1L), time = PassedTime(123.milliseconds)),
            timeMomentModel(id = TimeMomentId(2L), time = PassedTime(1234.milliseconds)),
            timeMomentModel(id = TimeMomentId(3L), time = PassedTime(123000.milliseconds)),
            timeMomentModel(id = TimeMomentId(4L), time = PassedTime(300000.milliseconds)),
            timeMomentModel(id = TimeMomentId(5L), time = PassedTime(5.4e+6.milliseconds)),
            timeMomentModel(id = TimeMomentId(7L), time = PassedTime(8.64e+7.milliseconds)),
        )
    ), onViewAction = {})
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

//    val animationTargetState = remember {
//        mutableStateOf(
//            state.state
//        )
//    }
//    if (state.isAnimating && state.state is TimeTravelState.Travelling) {
//        LaunchedEffect(key1 = state.isAnimating) {
//            animationTargetState.value = state.state
//        }
//    }
    val indexDuration = 3000

//    val transition = updateTransition(
//        targetState = animationTargetState.value, label = "main transition"
//    )
//
//    val time = transition.animateFloat(
//        transitionSpec = {
//            tween(durationMillis = indexDuration, easing = LinearEasing)
//        }, label = "traveller position"
//    ) { targetState ->
//        when (targetState) {
//            is TimeTravelState.Stationary -> 0f
//            is TimeTravelState.Travelling -> indexDuration.toFloat()
//        }
//    }
    val time = remember { Animatable(0f) }
    LaunchedEffect(state.state) {
        if (state.isAnimating && state.state is TimeTravelState.Travelling) {
            launch {
                time.animateTo(
                    targetValue = indexDuration.toFloat(),
                    animationSpec = tween(durationMillis = indexDuration, easing = LinearEasing)
                )
            }
        } else {
            time.snapTo(0f)
        }
    }

    LaunchedEffect(time.value) {
        if (time.value >= indexDuration) {
            onViewAction(MainViewAction.FinishedAnimation)
        }
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
                radius = pointSize * 0.5f,
                center = model.position
            )
            if (model.id == state.lastSelectedMomentId) {
                drawCircle(
                    color = selectedPointColor,
                    radius = pointSize * 0.6f,
                    center = model.position
                )
                drawCircle(
                    color = pointColor,
                    radius = pointSize * 0.5f,
                    center = model.position
                )
            }
            drawIntoCanvas {
                val lineBreakIndex = model.title.indexOf("\n")

                if (lineBreakIndex == 1) {
                    it.nativeCanvas.drawText(
                        model.title,
                        model.position.x,
                        model.position.y + textSize / 2,
                        textPaint
                    )
                } else {
                    val firstLine = model.title.substring(0, lineBreakIndex)
                    val secondLine = model.title.substring(lineBreakIndex, model.title.length)
                    val textBlockHeight = textSize * 2
                    it.nativeCanvas.drawText(
                        firstLine,
                        model.position.x,
                        model.position.y + textSize / 2 - textSize / 2,
                        textPaint
                    )
                    it.nativeCanvas.drawText(
                        secondLine,
                        model.position.x,
                        model.position.y + textSize / 2 + textSize / 2,
                        textPaint
                    )
                }


            }
        }

        if (state.moments.size >= 2 && state.isAnimating && state.state is TimeTravelState.Travelling) {
            val start = state.state.start
            val destination = state.state.destination
            val nodePath = formTimelinePath(
                moments = state.moments,
                start = start,
                destination = destination,
            )

            val momentIndex = calculateIndex(
                time = time.value,
                duration = indexDuration.toFloat(),
                nodes = nodePath.indices.toList(),
            )

            val nodeModels = timelineState.moments.associateBy { it.id }
            val startNode = nodeModels[nodePath[momentIndex]]!!
            val destinationNode = nodeModels[nodePath[momentIndex + 1]]!!

            val travelerPosition = calculateSegmentFraction(
                momentIndex = momentIndex,
                time = time.value,
                duration = indexDuration.toFloat(),
                nodes = nodePath.indices.toList(),
            )
//        Timber.d(
//            """
//                Moment index: ${momentIndex}
//                Time: ${time.value}
//                Fraction: ${travelerPosition}
//                Start position: ${startNode.position}
//                End position: ${destinationNode.position}
//            """.trimIndent()
//        )

            drawCircle(
                color = Color.Red,
                radius = pointSize * 0.2f,
                center = lerp(
                    start = startNode.position,
                    stop = destinationNode.position,
                    fraction = travelerPosition
                )
            )
        }
    }
}

fun timeTravelStatePreviewData(
    moments: List<TimeMomentModel> = listOf(
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
    )
): TimeTravelViewState = TimeTravelViewState(
    moments = moments.mapIndexed { index, model ->
        if (index > 0 && model.momentParents.isEmpty()) {
            model.copy(
                momentParents = listOf(moments[index - 1].id)
            )
        } else {
            model
        }
    },
    isAnimating = false,
    lastSelectedMomentId = TimeMomentId(1L),
)
