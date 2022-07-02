package com.daniil.shevtsov.timetravel.feature.info.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.info.presentation.InfoViewState
import com.daniil.shevtsov.timetravel.feature.info.presentation.infoViewState

@Preview
@Composable
fun InfoPreview() {
    InfoComposable(
        state = infoViewState(
            presentTags = listOf(
                "Time travel Available",
                "Nuclear Wasteland"
            )
        ),
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(8.dp)
    )
}

@Composable
fun InfoComposable(
    state: InfoViewState,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
        modifier = modifier,
    ) {
        Text(
            style = AppTheme.typography.title,
            text = "Tags",
            color = AppTheme.colors.textLight,
        )
        Text(
            style = AppTheme.typography.body,
            text = state.presentTags.joinToString(separator = "\n"),
            color = AppTheme.colors.textDark,
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundLight)
                .padding(AppTheme.dimensions.paddingS)
        )
    }

}
