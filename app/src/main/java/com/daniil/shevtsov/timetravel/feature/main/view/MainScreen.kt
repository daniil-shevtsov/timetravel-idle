package com.daniil.shevtsov.timetravel.feature.main.view

import android.graphics.Typeface
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewState
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.TimelineSizes
import com.daniil.shevtsov.timetravel.feature.timeline.presentation.timelinePresentation
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    MainScreen(
        state = MainViewState.Content(
            plot = PlotViewState(
                text = "Very important plot",
                choices = listOf(
                    ChoiceModel(id = ChoiceId(1L), text = "Do something smart"),
                    ChoiceModel(id = ChoiceId(2L), text = "Do something stupid"),
                )
            ),
            resources = ResourcesViewState(
                passedTime = ResourceModel(
                    id = ResourceId.Time,
                    title = "Passed Time",
                    text = "5.00 s"
                ),
                resources = listOf(
                    ResourceModel(
                        id = ResourceId.Money,
                        title = "Money",
                        text = "100 $"
                    )
                )
            ),
            actions = listOf(
                ActionModel(id = ActionId(0L), title = "Do Lol"),
                ActionModel(id = ActionId(1L), title = "Do Kek"),
                ActionModel(id = ActionId(2L), title = "Do Cheburek"),
            ),
            timeTravel = TimeTravelViewState(
                moments = listOf(
                    timeMomentModel(
                        id = TimeMomentId(1L),
                        time = PassedTime(4L.seconds),
                        momentParent = null,
                    ),
                    timeMomentModel(
                        id = TimeMomentId(2L),
                        time = PassedTime(8L.seconds),
                        momentParent = TimeMomentId(1),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(3L),
                        time = PassedTime(10L.seconds),
                        momentParent = TimeMomentId(2),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(4L),
                        time = PassedTime(11L.seconds),
                        momentParent = TimeMomentId(3),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(5L),
                        time = PassedTime(12L.seconds),
                        momentParent = TimeMomentId(4),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(6L),
                        time = PassedTime(13L.seconds),
                        momentParent = TimeMomentId(5),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(7L),
                        time = PassedTime(15L.seconds),
                        timelineParent = TimeMomentId(2L),
                        momentParent = TimeMomentId(2),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(8L),
                        time = PassedTime(16L.seconds),
                        timelineParent = TimeMomentId(2L),
                        momentParent = TimeMomentId(7),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(9L),
                        time = PassedTime(17L.seconds),
                        timelineParent = TimeMomentId(7L),
                        momentParent = TimeMomentId(8),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(10L),
                        time = PassedTime(18L.seconds),
                        timelineParent = TimeMomentId(7L),
                        momentParent = TimeMomentId(9),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(11L),
                        time = PassedTime(19L.seconds),
                        timelineParent = TimeMomentId(7L),
                        momentParent = TimeMomentId(10),
                    ),
                ),
                lastSelectedMomentId = TimeMomentId(1L),
            )
        ),
        onViewAction = {},
    )
}

@Composable
fun MainScreen(
    state: MainViewState,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit,
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Content -> Content(
            state = state,
            onViewAction = onViewAction,
            modifier = modifier,
        )
    }

}

@Composable
fun LoadingContent() {
    Text("Loading")
}


@Composable
fun Content(
    state: MainViewState.Content,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundDark)
            .padding(AppTheme.dimensions.paddingS)
    ) {
        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            (listOf(state.resources.passedTime) + state.resources.resources).forEach { resource ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)) {
                    Text(
                        text = resource.title,
                        color = AppTheme.colors.textLight,
                        style = AppTheme.typography.bodyTitle,
                        modifier = Modifier,
                    )
                    Text(
                        text = resource.text,
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.textLight,
                        style = AppTheme.typography.body,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
        ) {
            Text(
                text = "Register time point",
                style = AppTheme.typography.bodyTitle,
                textAlign = TextAlign.Center,
                color = AppTheme.colors.textLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewAction(MainViewAction.RegisterTimePoint) }
                    .background(AppTheme.colors.background)
                    .padding(AppTheme.dimensions.paddingS)
            )
            TimelineCanvas(
                state = state,
                onViewAction = onViewAction,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
        ) {
            (0..state.actions.size step 2)
                .map { index -> state.actions[index] to state.actions.getOrNull(index + 1) }
                .forEach { (startAction, endAction) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
                    ) {
                        ActionItem(
                            model = startAction,
                            modifier = Modifier.let { modifier ->
                                if (endAction == null) {
                                    modifier.fillMaxWidth()
                                } else {
                                    modifier
                                }
                                    .weight(1f)
                                    .clickable { onViewAction(MainViewAction.SelectAction(id = startAction.id)) }
                            },
                        )
                        if (endAction != null) {
                            ActionItem(
                                model = endAction,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onViewAction(MainViewAction.SelectAction(id = endAction.id)) }
                            )
                        }

                    }
                }
        }

        Text(
            text = state.plot.text,
            textAlign = TextAlign.Start,
            style = AppTheme.typography.body,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundLight)
                .padding(AppTheme.dimensions.paddingS),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXS)
        ) {
            state.plot.choices.forEach { choice ->
                Text(
                    text = choice.text,
                    style = AppTheme.typography.bodyTitle,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.textLight,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onViewAction(MainViewAction.SelectChoice(id = choice.id)) }
                        .background(AppTheme.colors.background)
                        .padding(AppTheme.dimensions.paddingS)
                )
            }
        }
    }

}

fun Offset.distanceTo(offset: Offset) = sqrt((offset.x - x).pow(2) + (offset.y - y).pow(2))

@Composable
private fun TimelineCanvas(
    state: MainViewState.Content,
    onViewAction: (MainViewAction) -> Unit
) {
    val allTimelines = state.timeTravel.moments
        .groupBy { it.timelineParent }

    val pointSize = with(LocalDensity.current) { 40.dp.toPx() }
    val lineHeight = with(LocalDensity.current) { 8.dp.toPx() }
    val segmentLength = with(LocalDensity.current) { 10.dp.toPx() }
    val timelineOffset = with(LocalDensity.current) {
        Offset(
            x = 5.dp.toPx(),
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
        allTimelines = allTimelines,
        sizes = sizes
    )

    Canvas(
        modifier = Modifier
            .background(AppTheme.colors.backgroundDarkest)
            .horizontalScroll(rememberScrollState())
            .width(500.dp)
            .height(300.dp)
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
            if (model.id == state.timeTravel.lastSelectedMomentId) {
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

@Composable
private fun ActionItem(
    model: ActionModel,
    modifier: Modifier = Modifier,
) {
    Text(
        text = model.title,
        style = AppTheme.typography.bodyTitle,
        textAlign = TextAlign.Center,
        color = AppTheme.colors.textLight,
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingS)
    )
}
