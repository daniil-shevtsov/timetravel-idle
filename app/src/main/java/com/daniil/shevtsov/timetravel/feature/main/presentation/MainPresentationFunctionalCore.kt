package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.main.domain.MainFunctionalCoreState

fun mapMainViewState(
    state: MainFunctionalCoreState
): MainViewState {
    return createMainViewState(state)
}

private fun createMainViewState(state: MainFunctionalCoreState): MainViewState {
    return MainViewState.Success
}
