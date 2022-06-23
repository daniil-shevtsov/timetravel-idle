package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                    TimeMomentModel(id = TimeMomentId(1L), time = PassedTime(Duration.seconds(4L))),
                    TimeMomentModel(id = TimeMomentId(2L), time = PassedTime(Duration.seconds(8L))),
                    TimeMomentModel(
                        id = TimeMomentId(3L),
                        time = PassedTime(Duration.seconds(10L))
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
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onViewAction(MainViewAction.RegisterTimePoint) }
                    .background(AppTheme.colors.background)
                    .padding(AppTheme.dimensions.paddingS)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundDarkest)
                    .padding(AppTheme.dimensions.paddingS),
                horizontalArrangement = Arrangement.spacedBy(
                    AppTheme.dimensions.paddingS,
                    Alignment.CenterHorizontally
                )
            ) {
                items(state.timeTravel.moments) { item ->
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
            }
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
