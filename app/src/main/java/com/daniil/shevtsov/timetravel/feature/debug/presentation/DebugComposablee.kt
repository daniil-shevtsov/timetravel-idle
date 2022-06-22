package com.daniil.shevtsov.timetravel.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

@Preview(
    widthDp = 230,
    heightDp = 534,
)
@Composable
fun DebugComposablePreview() {
    DebugComposable(
        state = debugViewState(),
        onAction = {},
    )
}

@Composable
fun DebugComposable(
    state: DebugViewState,
    onAction: (DebugViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded1: Boolean by remember { mutableStateOf(false) }
    var expanded2: Boolean by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .wrapContentSize(Alignment.TopStart)
    ) {

    }
}
