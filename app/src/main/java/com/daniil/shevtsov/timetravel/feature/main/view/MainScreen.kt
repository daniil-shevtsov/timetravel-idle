package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewState
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState

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
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = state.plot.text,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title,
            modifier = Modifier.weight(1f)
                .fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
        ) {
            state.plot.choices.forEach { choice ->
                Text(
                    text = choice.text,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onViewAction(MainViewAction.SelectChoice(id = choice.id)) }
                        .background(AppTheme.colors.background)
                        .padding(AppTheme.dimensions.paddingSmall)
                )
            }
        }
    }


}
