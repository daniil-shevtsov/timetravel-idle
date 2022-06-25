package com.daniil.shevtsov.timetravel.feature.main.view

import android.graphics.Typeface
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.Dp
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
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import timber.log.Timber
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration

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
                        time = PassedTime(Duration.seconds(4L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(2L),
                        time = PassedTime(Duration.seconds(8L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(3L),
                        time = PassedTime(Duration.seconds(10L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(4L),
                        time = PassedTime(Duration.seconds(11L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(5L),
                        time = PassedTime(Duration.seconds(12L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(6L),
                        time = PassedTime(Duration.seconds(13L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(7L),
                        time = PassedTime(Duration.seconds(15L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(8L),
                        time = PassedTime(Duration.seconds(16L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(9L),
                        time = PassedTime(Duration.seconds(17L)),
                        timelineParent = TimeMomentId(7L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(10L),
                        time = PassedTime(Duration.seconds(18L)),
                        timelineParent = TimeMomentId(7L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(11L),
                        time = PassedTime(Duration.seconds(19L)),
                        timelineParent = TimeMomentId(7L),
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

//            TimeMoments(
//                state = state,
//                onViewAction = onViewAction,
//            )
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
                                }.weight(1f)
                                    .clickable { onViewAction(MainViewAction.SelectAction(id = startAction.id)) }
                            },
                        )
                        if (endAction != null) {
                            ActionItem(
                                model = endAction,
                                modifier = Modifier.weight(1f)
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

typealias Timeline = Map<TimeMomentId?, List<TimeMomentModel>>

@Composable
private fun TimeMoments(
    state: MainViewState.Content,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit
) {
    val timelineHeight = 60.dp
    val allTimelines = state.timeTravel.moments
        .groupBy { it.timelineParent }

    val stateRowMap: Map<TimeMomentId?, LazyListState> =
        allTimelines.keys.toList().map { it to rememberLazyListState() }.toMap()

    val itemWidth = 60.dp
    val itemSpacing = AppTheme.dimensions.paddingS
    val temporaryPaddingMap = mutableMapOf<TimeMomentId?, Dp>()

    val paddingMap =
        (allTimelines.entries.map { it.key to it.value })
            .mapIndexed { mapIndex, (timelineParentId, timeMoments) ->
                val parentTimeline = allTimelines.entries.find { (_, timeMoments) ->
                    timeMoments.any { moment -> moment.id == timelineParentId }
                }?.toPair()
                val parentTimeMoments = parentTimeline?.second.orEmpty()

                if (mapIndex == 0) {
                    temporaryPaddingMap[null] = Dp(0f)
                } else {

                    val parent = parentTimeMoments.find { it.id == timelineParentId }
                    requireNotNull(parent) { "Can't find timeline with parent moment" }
                    val itemsBeforeParentAndParent = parentTimeMoments
                        .mapIndexed { index, moment -> index to moment }
                        .count { (index, moment) -> index <= parentTimeMoments.indexOf(parent) }

                    temporaryPaddingMap[timelineParentId] =
                        (AppTheme.dimensions.paddingS.value + temporaryPaddingMap[parent.timelineParent]!!.value + itemsBeforeParentAndParent * itemWidth.value + (itemsBeforeParentAndParent - 1) * itemSpacing.value).dp
                }

                timelineParentId to temporaryPaddingMap[timelineParentId]!!
            }.toMap()

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
        ) {
            allTimelines.entries.forEach { (timelineParentId, timeMoments) ->
                Text(
                    text = timelineParentId?.let { "Timeline ${timelineParentId.value}-A" }
                        ?: "Main Timeline",
                    color = AppTheme.colors.textLight,
                    style = AppTheme.typography.bodyTitle,
                    modifier = Modifier.height(timelineHeight).wrapContentHeight(),
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)) {
            allTimelines.entries.forEach { (timelineParentId, timeMoments) ->
                val padding: Dp = paddingMap[timelineParentId]!!

                Timeline(
                    state = stateRowMap[timelineParentId]!!,
                    itemWidth = itemWidth,
                    timeMoments = timeMoments,
                    modifier = modifier.height(timelineHeight),
                    contentPadding = PaddingValues(
                        start = AppTheme.dimensions.paddingS + padding,
                        end = AppTheme.dimensions.paddingS + (paddingMap.values.sumOf { it.value.toDouble() }).dp - padding,
                        top = AppTheme.dimensions.paddingS,
                        bottom = AppTheme.dimensions.paddingS,
                    ),
                    onViewAction = onViewAction,
                )
            }
        }
    }

    stateRowMap.entries.forEach { (timelineOriginId, scrolledState) ->
        LaunchedEffect(scrolledState.firstVisibleItemScrollOffset) {
            stateRowMap.entries.forEach { (stateYId, stateToScroll) ->
                if (stateYId == null) {
                    Timber.d("stateY: firstVisibleItemIndex: ${stateToScroll.firstVisibleItemIndex} firstVisibleItemScrollOffset: ${stateToScroll.firstVisibleItemScrollOffset} stateX: firstVisibleItemIndex: ${scrolledState.firstVisibleItemIndex} firstVisibleItemScrollOffset: ${scrolledState.firstVisibleItemScrollOffset}")
                }

                if (stateYId != timelineOriginId) {
                    if (!stateToScroll.isScrollInProgress) {
                        stateToScroll.scrollToItem(
                            scrolledState.firstVisibleItemIndex,
                            scrolledState.firstVisibleItemScrollOffset
                        )
                    }
                }
            }
        }
    }
}

data class MomentPosition(
    val position: Offset
)

fun Offset.distanceTo(offset: Offset) = sqrt((offset.x - x).pow(2) + (offset.y - y).pow(2))

@Composable
private fun TimelineCanvas(
    state: MainViewState.Content,
    onViewAction: (MainViewAction) -> Unit
) {
    val timelineHeight = 60.dp
    val allTimelines = state.timeTravel.moments
        .groupBy { it.timelineParent }

    val stateRowMap: Map<TimeMomentId?, LazyListState> =
        allTimelines.keys.toList().map { it to rememberLazyListState() }.toMap()

    val itemWidth = 60.dp
    val itemSpacing = AppTheme.dimensions.paddingS
    val temporaryPaddingMap = mutableMapOf<TimeMomentId?, Dp>()

    val paddingMap =
        (allTimelines.entries.map { it.key to it.value })
            .mapIndexed { mapIndex, (timelineParentId, timeMoments) ->
                val parentTimeline = allTimelines.entries.find { (_, timeMoments) ->
                    timeMoments.any { moment -> moment.id == timelineParentId }
                }?.toPair()
                val parentTimeMoments = parentTimeline?.second.orEmpty()

                if (mapIndex == 0) {
                    temporaryPaddingMap[null] = Dp(0f)
                } else {

                    val parent = parentTimeMoments.find { it.id == timelineParentId }
                    requireNotNull(parent) { "Can't find timeline with parent moment" }
                    val itemsBeforeParentAndParent = parentTimeMoments
                        .mapIndexed { index, moment -> index to moment }
                        .count { (index, moment) ->
                            index <= parentTimeMoments.indexOf(
                                parent
                            )
                        }

                    temporaryPaddingMap[timelineParentId] =
                        (AppTheme.dimensions.paddingS.value + temporaryPaddingMap[parent.timelineParent]!!.value + itemsBeforeParentAndParent * itemWidth.value + (itemsBeforeParentAndParent - 1) * itemSpacing.value).dp
                }

                timelineParentId to temporaryPaddingMap[timelineParentId]!!
            }.toMap()

    val pointSize = with(LocalDensity.current) { 40.dp.toPx() }
    val lineHeight = with(LocalDensity.current) { 8.dp.toPx() }
    val segmentLength = with(LocalDensity.current) { 60.dp.toPx() }
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
    var momentPositions: Map<TimeMomentId, MomentPosition> by remember {
        mutableStateOf(
            mapOf()
        )
    }
    allTimelines.entries.forEachIndexed { timelineIndex, (timelineId, moments) ->
        val parentTimeline = allTimelines.entries.find { (_, moments) ->
            moments.any { it.id == timelineId }
        }?.value
        val parentMoment = parentTimeline?.find { it.id == timelineId }
        val splitPadding = parentMoment?.let { parentMoment ->
            momentPositions[parentMoment.id]?.position?.x
        } ?: 0f
        val horizontalPadding = canvasPadding + splitPadding
        val verticalPadding = canvasPadding + timelineIndex * (pointSize + 10)
        moments.forEachIndexed { index, moment ->
            horizontalPadding + index * segmentLength + pointSize / 2
            val circlePosition =
                horizontalPadding + index * segmentLength + pointSize / 2
            momentPositions = momentPositions.toMutableMap().apply {
                put(moment.id, MomentPosition(Offset(x = circlePosition, y = verticalPadding)))
            }.toMap()
        }
    }
    Canvas(
        modifier = Modifier
            .background(AppTheme.colors.backgroundDarkest)
            .horizontalScroll(rememberScrollState())
            .width(500.dp)
            .height(300.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val nearestMoment =
                            momentPositions.entries.minByOrNull { (momentId, position) ->
                                tapOffset.distanceTo(position.position)
                            }
                        if (nearestMoment != null
                            && nearestMoment.value.position.distanceTo(tapOffset) <= pointSize
                        ) {
                            onViewAction(MainViewAction.TravelBackToMoment(nearestMoment.key))
                        }
                    })
            }) {
        val maxLineLength = (allTimelines.entries
            .maxByOrNull { (id, moments) -> moments.size }?.value?.size?.let {
                if (it > 0) {
                    it - 1
                } else {
                    it
                }
            } ?: 0) * segmentLength

        allTimelines.entries.forEachIndexed { timelineIndex, (timelineId, moments) ->
            val parentTimeline = allTimelines.entries.find { (_, moments) ->
                moments.any { it.id == timelineId }
            }?.value
            val parentMoment = parentTimeline?.find { it.id == timelineId }
            val splitPadding = parentMoment?.let { parentMoment ->
                momentPositions[parentMoment.id]?.position?.x
            } ?: 0f
            val horizontalPadding = canvasPadding + splitPadding
            val verticalPadding = canvasPadding + timelineIndex * (pointSize + 10)

            drawLine(
                color = lineColor,
                strokeWidth = lineHeight,
                start = Offset(
                    horizontalPadding,
                    verticalPadding
                ),
                end = Offset(
                    horizontalPadding + maxLineLength,
                    verticalPadding
                ),
            )

            moments.forEachIndexed { index, moment ->
                if (index == 0 && parentMoment != null) {
                    val circlePosition = momentPositions[parentMoment.id]!!.position
                    drawLine(
                        color = lineColor,
                        strokeWidth = lineHeight,
                        start = circlePosition,
                        end = Offset(
                            horizontalPadding + pointSize / 2,
                            verticalPadding
                        ),
                    )
                    drawCircle(
                        color = pointColor,
                        radius = pointSize / 2,
                        center = circlePosition
                    )
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            parentMoment.time.value.toString(),
                            circlePosition.x,
                            circlePosition.y + textSize / 2,
                            textPaint
                        )
                    }
                }

                val circlePosition =
                    horizontalPadding + index * segmentLength + pointSize / 2
                drawCircle(
                    color = pointColor,
                    radius = pointSize / 2,
                    center = Offset(circlePosition, verticalPadding)
                )
                if (moment.id == state.timeTravel.lastSelectedMomentId) {
                    drawCircle(
                        color = selectedPointColor,
                        radius = pointSize * 0.5f,
                        center = Offset(circlePosition, verticalPadding)
                    )
                    drawCircle(
                        color = pointColor,
                        radius = pointSize * 0.40f,
                        center = Offset(circlePosition, verticalPadding)
                    )
                }
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        moment.time.value.toString(),
                        circlePosition,
                        verticalPadding + textSize / 2,
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
private fun Timeline(
    timeMoments: List<TimeMomentModel>,
    modifier: Modifier = Modifier,
    itemWidth: Dp,
    contentPadding: PaddingValues,
    state: LazyListState,
    onViewAction: (MainViewAction) -> Unit
) {
    LazyRow(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundDarkest),
        horizontalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.paddingS,
            Alignment.Start
        ),
        contentPadding = contentPadding,
    ) {
        items(timeMoments) { item ->
            TimeMoment(
                item = item,
                modifier = modifier.width(itemWidth).fillMaxHeight(),
                onViewAction = onViewAction
            )
        }
    }
}

@Composable
private fun TimeMoment(
    item: TimeMomentModel,
    modifier: Modifier,
    onViewAction: (MainViewAction) -> Unit
) {
    Text(
        text = item.time.value.toString(),
        style = AppTheme.typography.bodyTitle,
        textAlign = TextAlign.Center,
        color = AppTheme.colors.textLight,
        modifier = modifier
            .background(AppTheme.colors.background)
            .clickable { onViewAction(MainViewAction.TravelBackToMoment(id = item.id)) }
            .padding(AppTheme.dimensions.paddingS)
    )
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
