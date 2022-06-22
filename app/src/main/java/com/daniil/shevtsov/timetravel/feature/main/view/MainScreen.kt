package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.debug.presentation.debugViewState
import com.daniil.shevtsov.timetravel.feature.main.presentation.*
import com.google.accompanist.insets.statusBarsHeight

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    MainScreen(
        state = mainViewState(
            drawerState = DrawerViewState(
                tabSelectorState = emptyList(),
                drawerContent = DrawerContentViewState.Debug(
                    state = debugViewState()
                )
            ),
        ),
        onViewAction = {},
    )
}

@Composable
fun MainScreen(
    state: MainViewState,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.background(AppTheme.colors.background).fillMaxHeight(),
    ) {
        Spacer(
            modifier
                .statusBarsHeight()
                .fillMaxWidth()
        )
        Text("HELLO")
    }
}
