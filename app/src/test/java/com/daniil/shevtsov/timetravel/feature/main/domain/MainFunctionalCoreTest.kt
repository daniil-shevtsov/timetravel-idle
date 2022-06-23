package com.daniil.shevtsov.timetravel.feature.main.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.plot.domain.*
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import org.junit.jupiter.api.Test
import kotlin.time.Duration


class MainFunctionalCoreTest {

    @Test
    fun `should show initial plot`() {
        val initialPlot = plot(text = "Hello")

        val state = mainFunctionalCore(
            state = gameState(plot = initialPlot, passedTime = PassedTime(Duration.ZERO)),
            viewAction = MainViewAction.Init,
        )

        assertThat(state)
            .extractingPlot()
            .isEqualTo(initialPlot)
    }

    @Test
    fun `should show plot for selecteed choice`() {
        val lolPlot = plot(id = PlotId(2L), text = "You have chosen lol")
        val kekPlot = plot(id = PlotId(3L), text = "You have chosen kek")

        val lolChoice = choice(id = ChoiceId(1L), destinationPlotId = lolPlot.id)
        val kekChoice = choice(id = ChoiceId(2L), destinationPlotId = kekPlot.id)

        val initialPlot = plot(
            id = PlotId(1L),
            text = "Should you lol or kek?",
            choices = listOf(
                lolChoice,
                kekChoice,
            )
        )

        val initialState = gameState(
            plot = initialPlot,
            plots = listOf(
                initialPlot,
                lolPlot,
                kekPlot,
            ),
            passedTime = PassedTime(Duration.ZERO)
        )

        val lolState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectChoice(id = lolChoice.id)
        )
        val kekState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectChoice(id = kekChoice.id)
        )

        assertThat(lolState)
            .extractingPlot()
            .extractingText()
            .isEqualTo(lolPlot.text)

        assertThat(kekState)
            .extractingPlot()
            .extractingText()
            .isEqualTo(kekPlot.text)
    }

    private fun Assert<GameState>.extractingPlot() = prop(GameState::plot)
    private fun Assert<Plot>.extractingText() = prop(Plot::text)

}
