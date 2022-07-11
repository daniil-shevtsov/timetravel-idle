package com.daniil.shevtsov.timetravel.feature.timeline.view

import android.graphics.Typeface
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.daniil.shevtsov.timetravel.core.ui.widgets.MyButton
import com.daniil.shevtsov.timetravel.feature.main.view.distanceTo
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.TimelineSizes
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.timelinePresentation
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import timber.log.Timber

enum class AnimationDirection {
    Start,
    Destination,
}

//data class TimelineAnimationState(
//    val direction: AnimationDirection,
//    val momentKeys: List<TimeMomentId>,
//)

@Preview
@ExperimentalAnimationApi
@Composable
fun AnimationPrototypePreview() {
    AnimationPrototype()
}

@Composable
fun AnimationPrototype(
    modifier: Modifier = Modifier,
) {
    val state = timeTravelStatePreviewData()
    val allTimelines = state.moments
        .groupBy { it.timelineParent }

    val nodes = state.moments.associateBy { it.id }

    val nodePath = listOf(
        nodes[TimeMomentId(8L)]!!,
        nodes[TimeMomentId(7L)]!!,
        nodes[TimeMomentId(9L)]!!,
        nodes[TimeMomentId(10L)]!!,
        nodes[TimeMomentId(5L)]!!,
        nodes[TimeMomentId(6L)]!!,
    ).map { it.id }
    val animationTargetState = remember {
        mutableStateOf(
            AnimationDirection.Start
        )
    }
    val indexDuration = 3000

    val transition = updateTransition(
        targetState = animationTargetState.value, label = "main transition"
    )

    val time = transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = indexDuration, easing = LinearEasing)
        }, label = "traveller position"
    ) { targetState ->
        when (targetState) {
            AnimationDirection.Destination -> indexDuration.toFloat()
            AnimationDirection.Start -> 0f
        }
    }

    Column {
        MyButton(
            text = "Launch Animation",
            onClick = {
                animationTargetState.value = when (animationTargetState.value) {
                    AnimationDirection.Destination -> AnimationDirection.Start
                    AnimationDirection.Start -> AnimationDirection.Destination
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.paddingS)
        )

        TimelineCanvas(
            state = timeTravelStatePreviewData(),
            onViewAction = {},
        )

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
            Timber.d(
                """
                Moment index: ${momentIndex}
                Time: ${time.value}
                Fraction: ${travelerPosition}
                Start position: ${startNode.position}
                End position: ${destinationNode.position}
            """.trimIndent()
            )

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

fun calculateIndex(
    time: Float,
    duration: Float,
    nodes: List<Int>
): Int {
    val segments = nodes.size - 1
    val segmentDuration = duration / segments

    val kek = time / segmentDuration
    return (kek - 0.0001).toInt()
}

fun calculateSegmentFraction(
    momentIndex: Int,
    time: Float,
    duration: Float,
    nodes: List<Int>
): Float {
    val segments = nodes.size - 1
    val segmentDuration = duration / segments

    return time / segmentDuration - momentIndex
}


private enum class Direction {
    Past,
    Future
}

fun formTimelinePath(
    moments: List<TimeMomentModel>,
    start: TimeMomentId,
    destination: TimeMomentId,
): List<TimeMomentId> {


    val destinationMoment = moments.find { it.id == destination }!!
    val startMoment = moments.find { it.id == start }!!

    val destinationIsChild = areRelatives(
        child = destinationMoment.id,
        parent = startMoment.id,
        moments = moments,
    )
    val destinationIsParent = areRelatives(
        child = startMoment.id,
        parent = destinationMoment.id,
        moments = moments,
    )

    val direction = when {
        destinationIsChild -> Direction.Future
        else -> Direction.Past
    }

    var currentMoment = when (direction) {
        Direction.Future -> startMoment
        Direction.Past -> destinationMoment
    }
    val finalMoment = when (direction) {
        Direction.Future -> destinationMoment
        Direction.Past -> startMoment
    }
    val momentIds = mutableListOf<TimeMomentId>()
    var foundFinalMoment = false
    while (!foundFinalMoment) {
        if (!momentIds.contains(currentMoment.id)) {
            momentIds.add(currentMoment.id)
        }

        if (currentMoment.id == finalMoment.id) {
            foundFinalMoment = true
        } else if (currentMoment.momentParents.isNotEmpty()) {
            currentMoment = moments.find { it.id == currentMoment.momentParents.first() }!!
        }
    }
    return when (direction) {
        Direction.Past -> momentIds.reversed()
        Direction.Future -> momentIds
    }

}

private fun areRelatives(
    child: TimeMomentId,
    parent: TimeMomentId,
    moments: List<TimeMomentModel>
): Boolean {
    val momentIds = mutableListOf<TimeMomentId>()
    val oneMoment = moments.find { it.id == child }!!
    val childMoment = moments.find { it.id == parent }!!
    var isRelative = false
    var currentMoment = childMoment
    repeat(moments.size) {
        if (!momentIds.contains(currentMoment.id)) {
            momentIds.add(currentMoment.id)
        }
        if (currentMoment.id == oneMoment.id) {
            isRelative = true
        }
        if (currentMoment.momentParents.isNotEmpty()) {
            currentMoment = moments.find { it.id == currentMoment.momentParents.first() }!!
        }
    }

    return isRelative
}
