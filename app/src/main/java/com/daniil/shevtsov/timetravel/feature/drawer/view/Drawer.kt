package com.daniil.shevtsov.timetravel.feature.drawer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugComposable
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.*

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainDrawerDebugPreview() {
    MainDrawer(
        drawerState = drawerViewState(
            tabSelectorState = listOf(
                drawerTab(
                    id = DrawerTabId.Debug,
                    title = "Debug",
                    isSelected = true
                ),
            ),
            drawerContent = drawerDebugContent()
        ),
        onViewAction = {},
    )
}

@Composable
fun MainDrawer(
    drawerState: DrawerViewState,
    onViewAction: (DrawerViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DrawerTabSelector(
        tabs = drawerState.tabSelectorState,
        onTabSelected = { id -> onViewAction(DrawerViewAction.TabSwitched(id = id)) },
    )
    when (val drawerContentState = drawerState.drawerContent) {
        is DrawerContentViewState.Debug -> {
            DebugComposable(
                state = drawerContentState.state,
                modifier = modifier
                    .background(AppTheme.colors.background)
                    .padding(8.dp),
                onAction = { debugAction ->
                    onViewAction(DrawerViewAction.Debug(action = debugAction))
                })
        }
    }
}
