package com.daniil.shevtsov.timetravel.feature.main.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.navigation.GeneralViewAction
import com.daniil.shevtsov.timetravel.core.navigation.ScreenHostViewModel
import com.daniil.shevtsov.timetravel.core.navigation.ScreenViewAction
import com.daniil.shevtsov.timetravel.core.navigation.ScreenViewState
import com.daniil.shevtsov.timetravel.core.ui.Pallete
import com.daniil.shevtsov.timetravel.feature.debug.presentation.DebugComposable
import com.daniil.shevtsov.timetravel.feature.debug.presentation.debugViewState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.timetravel.feature.drawer.view.DrawerTabSelector
import com.daniil.shevtsov.timetravel.feature.main.presentation.*
import com.google.accompanist.insets.statusBarsHeight

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainDrawerDebugPreview() {
    MainDrawer(
        state = mainViewState(
            drawerState = drawerViewState(
                tabSelectorState = listOf(
                    drawerTab(
                        id = DrawerTabId.Debug,
                        title = "Debug",
                        isSelected = true
                    ),
                ),
                drawerContent = drawerDebugContent()
            )
        ),
        onViewAction = {},
    )
}

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
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel
) {
    val delegatedViewState by viewModel.state.collectAsState()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }

    when (val viewState = delegatedViewState) {
        is ScreenViewState.Main -> {
            MainScreen(
                state = viewState.state,
                onViewAction = { action -> viewModel.handleAction(ScreenViewAction.Main(action)) },
            )
        }
    }
}

@Composable
fun MainScreen(
    state: MainViewState,
    onViewAction: (MainViewAction) -> Unit,
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
            onViewAction = onViewAction,
        )
    }

}

@Composable
fun LoadingContent() {
    Text("Loading")
}

@Composable
fun SuccessContent(
    state: MainViewState.Success,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { MainDrawer(state, onViewAction) },
        content = {
            ContentBody(
                state = state,
                onViewAction = onViewAction,
            )
        }
    )
}

@Composable
private fun MainDrawer(
    state: MainViewState.Success,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DrawerTabSelector(
        tabs = state.drawerState.tabSelectorState,
        onTabSelected = { id -> onViewAction(MainViewAction.DrawerTabSwitched(id = id)) },
    )
    when (val drawerContentState = state.drawerState.drawerContent) {
        is DrawerContentViewState.Debug -> {
            DebugComposable(
                state = drawerContentState.state,
                modifier = modifier
                    .background(Pallete.Red)
                    .padding(8.dp),
                onAction = { action ->

                })
        }
    }
}

@Composable
fun ContentBody(
    state: MainViewState.Success,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Pallete.Red),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(
            modifier
                .statusBarsHeight()
                .fillMaxWidth()
        )
        Text("HELLO")
    }
}

