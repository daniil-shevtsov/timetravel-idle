package com.daniil.shevtsov.timetravel.feature.main.presentation

import assertk.Assert
import assertk.assertions.isInstanceOf
import assertk.assertions.prop


class MainPresentationTest {


    private fun Assert<MainViewState>.extractingMainState() =
        isInstanceOf(MainViewState.Success::class)

    private fun Assert<MainViewState>.extractingDebugState() = extractingMainState()
        .prop(MainViewState.Success::drawerState)
        .prop(DrawerViewState::drawerContent)
        .isInstanceOf(DrawerContentViewState.Debug::class)
        .prop(DrawerContentViewState.Debug::state)

}
