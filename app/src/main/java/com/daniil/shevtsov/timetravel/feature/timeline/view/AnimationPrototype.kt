package com.daniil.shevtsov.timetravel.feature.timeline.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

enum class AnimationState {
    Collapsed,
    Expanded
}

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
    val animationTargetState = remember {
        mutableStateOf(AnimationState.Collapsed)
    }

// Any state change will trigger animations which
// are created with this transition to the new state
    val transition = updateTransition(
        targetState = animationTargetState.value
    )

    val circleAlpha = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) }
    ) {
        if (it == AnimationState.Collapsed) 0f else 1f
    }

    val circleRadius = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) }
    ) {
        if (it == AnimationState.Collapsed) 0f else 1f
    }
    Column {
        Button(onClick = {
            animationTargetState.value = when (animationTargetState.value) {
                AnimationState.Collapsed -> AnimationState.Expanded
                AnimationState.Expanded -> AnimationState.Collapsed
            }
        }, colors = ButtonColors()) {
            Text(
                "Launch animation",
                color = AppTheme.colors.textLight,
                modifier = Modifier
            )
        }

        Canvas(
            modifier = modifier
                .background(backgroundColor)
                .size(300.dp)
        ) {
            val canvasSize = size
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawRoundRect(
                size = (canvasSize / 2f) * circleRadius.value,
                color = lineColor.copy(alpha = circleAlpha.value),
                topLeft = Offset(
                    x = canvasWidth / 4F,
                    y = canvasHeight / 3F
                )
            )

        }
    }

}
