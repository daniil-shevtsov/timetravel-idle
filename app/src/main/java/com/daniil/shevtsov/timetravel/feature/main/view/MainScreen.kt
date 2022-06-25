package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
                        time = PassedTime(Duration.seconds(10L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(8L),
                        time = PassedTime(Duration.seconds(11L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(9L),
                        time = PassedTime(Duration.seconds(12L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(10L),
                        time = PassedTime(Duration.seconds(13L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(11L),
                        time = PassedTime(Duration.seconds(14L)),
                        timelineParent = TimeMomentId(2L),
                    ),
                )
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

            TimeMoments(
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

@Composable
private fun TimeMoments(
    state: MainViewState.Content,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit
) {
    val timelineHeight = 60.dp
    val titleHeight = 20.dp
    val mainTimeline = state.timeTravel.moments.filter { it.timelineParent == null }
    val otherTimelines = state.timeTravel.moments
        .filter { it.timelineParent != null }
        .groupBy { it.timelineParent }

    val stateRowX = rememberLazyListState() // State for the first Row, X
    val stateRowY = rememberLazyListState() // State for the second Row, Y


    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
        ) {
            Text(
                text = "Main Timeline",
                color = AppTheme.colors.textLight,
                style = AppTheme.typography.bodyTitle,
                modifier = Modifier.height(timelineHeight).wrapContentHeight(),
            )
            otherTimelines.entries.forEach { (timelineParentId, timeMoments) ->
                Text(
                    text = "Timeline ${timelineParentId?.value}-A",
                    color = AppTheme.colors.textLight,
                    style = AppTheme.typography.bodyTitle,
                    modifier = Modifier.height(timelineHeight).wrapContentHeight(),
                )
            }
        }
        val itemWidth = 60.dp
        val itemSpacing = AppTheme.dimensions.paddingS
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)) {
            Timeline(
                state = stateRowX,
                itemWidth = itemWidth,
                timeMoments = mainTimeline,
                modifier = modifier.height(timelineHeight),
                onViewAction = onViewAction,
                contentPadding = PaddingValues(AppTheme.dimensions.paddingS)
            )
            otherTimelines.entries.forEach { (timelineParentId, timeMoments) ->
                val parent = mainTimeline.find { it.id == timelineParentId }!!
                val itemsBeforeParentAndParent = mainTimeline
                    .mapIndexed { index, moment -> index to moment }
                    .count { (index, moment) -> index <= mainTimeline.indexOf(parent) }
                val padding =
                    (itemsBeforeParentAndParent * itemWidth.value + (itemsBeforeParentAndParent - 1) * itemSpacing.value + AppTheme.dimensions.paddingS.value).dp
                Timeline(
                    state = stateRowY,
                    itemWidth = itemWidth,
                    timeMoments = timeMoments,
                    modifier = modifier.height(timelineHeight),
                    contentPadding = PaddingValues(
                        start = AppTheme.dimensions.paddingS + padding,
                        end = AppTheme.dimensions.paddingS,
                        top = AppTheme.dimensions.paddingS,
                        bottom = AppTheme.dimensions.paddingS,
                    ),
                    onViewAction = onViewAction,
                )
            }
        }
    }

    LaunchedEffect(stateRowX.firstVisibleItemScrollOffset) {
        stateRowY.scrollToItem(
            stateRowX.firstVisibleItemIndex,
            stateRowX.firstVisibleItemScrollOffset
        )
    }

    LaunchedEffect(stateRowY.firstVisibleItemScrollOffset) {
        stateRowX.scrollToItem(
            stateRowY.firstVisibleItemIndex,
            stateRowY.firstVisibleItemScrollOffset
        )
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
