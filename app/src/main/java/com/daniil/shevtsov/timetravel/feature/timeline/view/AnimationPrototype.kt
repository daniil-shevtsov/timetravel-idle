package com.daniil.shevtsov.timetravel.feature.timeline.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

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

    val scale = scaleShapeTransition(0.1f, 1f, 2000)
    val animatedLineColor = colorShapeTransition(lineColor, pointColor, 2000)

    Canvas(modifier = modifier
        .background(backgroundColor)
        .size(300.dp)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }) {
        val canvasSize = size
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRoundRect(
            size = canvasSize / 2F,
            color = animatedLineColor,
            topLeft = Offset(
                x = canvasWidth / 4F,
                y = canvasHeight / 3F
            )
        )

    }
}

@Composable
fun colorShapeTransition(
    initialValue: Color,
    targetValue: Color,
    durationMillis: Int
): Color {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    return color
}

@Composable
fun scaleShapeTransition(
    initialValue: Float,
    targetValue: Float,
    durationMillis: Int
): Float {
    val infiniteTransition = rememberInfiniteTransition()
    val scale: Float by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis),
            repeatMode = RepeatMode.Restart
        )
    )

    return scale
}
