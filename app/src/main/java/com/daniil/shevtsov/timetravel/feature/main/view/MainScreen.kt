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
    heightDp = 734,
)
@Composable
fun MainPreview() {
    MainScreen(
        state = MainViewState.Content(
            plot = PlotViewState(
                text = "Very important plot",
                choices = listOf(
                    ChoiceModel(id = ChoiceId(1L), text = "Choose something smart"),
                    ChoiceModel(id = ChoiceId(2L), text = "Choose something stupid stupid"),
                    ChoiceModel(id = ChoiceId(3L), text = "Choose something cool"),
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
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundDark)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
            modifier = Modifier
                .heightIn(max = maxHeight)
                .verticalScroll(rememberScrollState())
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

            ActionsPane(
                state = state,
                onViewAction = onViewAction,
            )

            Text(
                text = state.plot.text,
                textAlign = TextAlign.Start,
                style = AppTheme.typography.body,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundLight)
                    .padding(AppTheme.dimensions.paddingS),
            )
            ButtonPane(items = state.plot.choices) { item, modifier ->
                MyButton(
                    text = item.text,
                    onClick = { onViewAction(MainViewAction.SelectChoice(id = item.id)) },
                    modifier = modifier
                )
            }
        }
    }


}

@Composable
private fun ActionsPane(
    state: MainViewState.Content,
    onViewAction: (MainViewAction) -> Unit
) {
    ButtonPane(items = state.actions) { item: ActionModel, modifier ->
        MyButton(
            text = item.title,
            onClick = { onViewAction(MainViewAction.SelectAction(id = item.id)) },
            modifier = modifier
        )
    }
}

@Composable
private fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AppTheme.typography.bodyTitle,
        textAlign = TextAlign.Center,
        color = AppTheme.colors.textLight,
        modifier = modifier
            .background(AppTheme.colors.background)
            .clickable { onClick() }
            .padding(AppTheme.dimensions.paddingS)
    )
}

@Composable
private fun <T> ButtonPane(
    items: List<T>,
    itemContent: @Composable (item: T, modifier: Modifier) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
    ) {
        (items.indices step 2)
            .map { index -> items[index] to items.getOrNull(index + 1) }
            .forEach { (startAction, endAction) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {
                    itemContent(startAction, Modifier.let { modifier ->
                        if (endAction == null) {
                            modifier.fillMaxWidth()
                        } else {
                            modifier
                        }
                            .weight(1f)
                            .fillMaxHeight()
                    })
                    if (endAction != null) {
                        itemContent(
                            endAction, Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
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
            .height(300.dp) //TODO: Add vertical scroll
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
