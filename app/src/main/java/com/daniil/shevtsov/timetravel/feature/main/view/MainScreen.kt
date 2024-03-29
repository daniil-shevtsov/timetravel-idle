package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.core.ui.widgets.MyButton
import com.daniil.shevtsov.timetravel.core.ui.widgets.Pane
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.location.view.LocationComposable
import com.daniil.shevtsov.timetravel.feature.location.view.locationPreviewData
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewState
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.view.ResourcesPane
import com.daniil.shevtsov.timetravel.feature.resources.view.resourcesPanePreviewData
import com.daniil.shevtsov.timetravel.feature.timeline.view.TimelineCanvas
import com.daniil.shevtsov.timetravel.feature.timeline.view.timeTravelStatePreviewData
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import kotlin.math.pow
import kotlin.math.sqrt

@Preview(
    widthDp = 320,
    heightDp = 1034,
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
            resources = resourcesPanePreviewData(),
            actions = listOf(
                ActionModel(id = ActionId(0L), title = "Do Lol"),
                ActionModel(id = ActionId(1L), title = "Do Kek multiline very long"),
                ActionModel(id = ActionId(2L), title = "Do Cheburek"),
            ),
            timeTravel = timeTravelStatePreviewData(),
            location = locationPreviewData(),
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
            ResourcesPane(
                state = state.resources,
                onTransferResource = { resourceId, direction, amount ->
                    onViewAction(
                        MainViewAction.TransferResource(
                            id = resourceId,
                            direction = direction,
                            amount = amount,
                        )
                    )
                },
            )
            LocationComposable(
                state = state.location,
                onViewAction = onViewAction,
            )
            ActionsPane(
                state = state,
                onViewAction = onViewAction,
            )
            PlotPane(
                state = state.plot,
                onViewAction = onViewAction,
            )
            TimelinePane(
                state = state.timeTravel,
                onViewAction = onViewAction,
            )
        }
    }


}

@Composable
private fun PlotPane(
    state: PlotViewState,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    WithTitle(title = "Plot", modifier = modifier) {
        Text(
            text = state.text,
            textAlign = TextAlign.Start,
            style = AppTheme.typography.body,
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundText)
                .padding(AppTheme.dimensions.paddingS),
        )
        Pane(items = state.choices) { item, modifier ->
            MyButton(
                text = item.text,
                onClick = { onViewAction(MainViewAction.SelectChoice(id = item.id)) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun TimelinePane(
    state: TimeTravelViewState,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    WithTitle(title = "Timeline", modifier = modifier) {
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
}

@Composable
private fun ActionsPane(
    state: MainViewState.Content,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    WithTitle(title = "Actions", modifier = modifier) {
        Pane(items = state.actions) { item: ActionModel, modifier ->
            MyButton(
                text = item.title,
                onClick = { onViewAction(MainViewAction.SelectAction(id = item.id)) },
                modifier = modifier
            )
        }
    }
}

@Composable
fun WithTitle(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXS),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = AppTheme.typography.subtitle,
            color = AppTheme.colors.textLight,
        )
        content()
    }
}

fun Offset.distanceTo(offset: Offset) = sqrt((offset.x - x).pow(2) + (offset.y - y).pow(2))
