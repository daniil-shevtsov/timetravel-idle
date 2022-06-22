package com.daniil.shevtsov.timetravel.feature.debug.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.timetravel.core.ui.Pallete
import com.daniil.shevtsov.timetravel.core.ui.cavitary

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
            .background(Pallete.Red)
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Column {

        }
    }
}
