package com.daniil.shevtsov.timetravel.core.navigation

import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class ScreenPresentationTest {
    @Test
    fun `should form main view state when main screen selected`() {
        val state = gameState(currentScreen = Screen.Main)

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState).isInstanceOf(ScreenViewState.Main::class)
    }
}
