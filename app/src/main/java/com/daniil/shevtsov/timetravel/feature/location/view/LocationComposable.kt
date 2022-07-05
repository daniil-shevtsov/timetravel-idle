package com.daniil.shevtsov.timetravel.feature.location.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.domain.SelectorKey
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.Selector
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.selectorPreviewData
import com.daniil.shevtsov.timetravel.feature.location.domain.LocationId
import com.daniil.shevtsov.timetravel.feature.location.presentation.LocationViewState
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction

@Preview
@Composable
fun LocationPreview() {
    LocationComposable(
        state = locationPreviewData(),
        onViewAction = {},
    )
}

@Composable
fun LocationComposable(
    state: LocationViewState,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXS),
        modifier = modifier,
    ) {
        Selector(
            state = state.selector,
            onSelected = { selectorId ->
                onViewAction(MainViewAction.SelectLocation(id = LocationId(selectorId.raw)))
            },
            onExpandChange = { onViewAction(MainViewAction.ToggleExpanded(key = SelectorKey.Location)) }
        )
        Text(
            text = state.description,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundText)
                .padding(AppTheme.dimensions.paddingXS)
        )
    }
}

fun locationPreviewData() = LocationViewState(
    selector = selectorPreviewData(),
    description = "Very important description of location"
)
