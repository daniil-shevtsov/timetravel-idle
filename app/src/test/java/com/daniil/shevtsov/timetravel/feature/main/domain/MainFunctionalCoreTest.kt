package com.daniil.shevtsov.timetravel.feature.main.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import org.junit.jupiter.api.Test


class MainFunctionalCoreTest {

    @Test
    fun `should show initial plot`() {
        val initialPlot = "Hello"

        val state = mainFunctionalCore(
            state = gameState(plot = initialPlot),
            viewAction = MainViewAction.Init,
        )

        assertThat(state)
            .extractingPlot()
            .isEqualTo(initialPlot)
    }

    private fun Assert<GameState>.extractingPlot() = prop(GameState::plot)

}
