package com.daniil.shevtsov.timetravel.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.drawer.view.MainDrawer
import com.daniil.shevtsov.timetravel.feature.main.view.MainScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel,
    modifier: Modifier = Modifier,
) {
    val delegatedViewState by viewModel.state.collectAsState()

    val systemUiController = rememberSystemUiController()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }
    AppTheme {
        val toolbarColor = AppTheme.colors.background
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = toolbarColor,
            )
        }

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                MainDrawer(
                    drawerState = delegatedViewState.drawerState,
                    modifier = modifier,
                    onViewAction = { action -> viewModel.handleAction(ScreenViewAction.Drawer(action)) })
            },
            content = {
                when (val contentViewState = delegatedViewState.contentState) {
                    is ScreenContentViewState.Main -> {
                        MainScreen(
                            state = contentViewState.state,
                            modifier = modifier,
                            onViewAction = { action ->
                                viewModel.handleAction(
                                    ScreenViewAction.Main(
                                        action
                                    )
                                )
                            },
                        )
                    }
                    ScreenContentViewState.Loading -> Unit
                }
            }
        )
    }
}
