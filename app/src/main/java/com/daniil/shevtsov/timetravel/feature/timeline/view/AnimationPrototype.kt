package com.daniil.shevtsov.timetravel.feature.timeline.view

import android.graphics.Typeface
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
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
import com.daniil.shevtsov.timetravel.feature.main.view.distanceTo
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.TimelineSizes
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.timelinePresentation
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

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
    val lineColor = AppTheme.colors.textLight
    val pointColor = AppTheme.colors.background
    val selectedPointColor = AppTheme.colors.backgroundLight
    val backgroundColor = AppTheme.colors.backgroundDarkest

// Need to remember in order to prevent setting
// the same state value to the transition during
// recomposition.
    val state = timeTravelStatePreviewData()
    val allTimelines = state.moments
        .groupBy { it.timelineParent }

    val nodes = state.moments.associateBy { it.id }

//    val nodePath = listOf(
//        nodes[TimeMomentId(6L)]!!,
//        nodes[TimeMomentId(5L)]!!,
//        nodes[TimeMomentId(10L)]!!,
//        nodes[TimeMomentId(9L)]!!,
//    )
    val nodePath = listOf(
        nodes[TimeMomentId(1L)]!!,
        nodes[TimeMomentId(2L)]!!,
        nodes[TimeMomentId(3L)]!!,
//        nodes[TimeMomentId(4L)]!!,
//        nodes[TimeMomentId(5L)]!!,
//        nodes[TimeMomentId(6L)]!!,
    )
    val animationTargetState = remember {
        mutableStateOf(
            AnimationDirection.Start
        )
    }

// Any state change will trigger animations which
// are created with this transition to the new state
    val transition = updateTransition(
        targetState = animationTargetState.value, label = "main transition"
    )
//
    val momentIndex = transition.animateInt(
        transitionSpec = { tween(durationMillis = 3000) }, label = "moment index animation"
    ) { targetState ->
        when(targetState) {
            AnimationDirection.Destination -> nodePath.lastIndex - 1
            AnimationDirection.Start -> 0
        }
    }

    val floatMap = nodePath.indices.associate { it to 1f }

    val travelerPosition = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) }, label = "traveller position"
    ) { targetState ->
        when(targetState) {
            AnimationDirection.Destination -> 1f
            AnimationDirection.Start -> 0f
        }
    }
    Column {
        Text(
            "Launch animation",
            color = AppTheme.colors.textLight,
            modifier = Modifier
                .background(AppTheme.colors.background)
                .fillMaxWidth()
                .clickable {
                    animationTargetState.value = when (animationTargetState.value) {
                        AnimationDirection.Destination -> AnimationDirection.Start
                        AnimationDirection.Start -> AnimationDirection.Destination
                    }
                }
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
            allMoments = allTimelines.values.flatten().filter { it.id.value <= 3L },
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
            val nodeModels = timelineState.moments.associateBy { it.id }
            val startNode = nodeModels[nodePath[momentIndex.value].id]!!
            val destinationNode = nodeModels[nodePath[momentIndex.value + 1].id]!!

            // 0 0
            // 0 1
            // 1 0
            // 1 1
            // 2 0
            // 2 1
            // ...
            // 10 0
            // 10 1

            drawCircle(
                color = Color.Red,
                radius = pointSize * 0.2f,
                center = lerp(
                    start = startNode.position,
                    stop = destinationNode.position,
                    fraction = travelerPosition.value
                )
            )
        }
    }

}
