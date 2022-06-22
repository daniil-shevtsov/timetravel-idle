package com.daniil.shevtsov.timetravel.feature.main.presentation


sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val drawerState: DrawerViewState,
    ) : MainViewState()
}
