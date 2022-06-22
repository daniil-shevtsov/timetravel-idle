package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.core.navigation.ScreenContentViewState
import com.daniil.shevtsov.timetravel.core.navigation.ScreenHostViewState
import com.daniil.shevtsov.timetravel.core.navigation.screenPresentationFunctionalCore
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class ScreenPresentationTest {
    @Test
    fun `should form main view state when main screen selected`() {
        val state = gameState(currentScreen = Screen.Main, plot = "Hello")

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState)
            .prop(ScreenHostViewState::contentState)
            .isInstanceOf(ScreenContentViewState.Main::class)
    }
}
