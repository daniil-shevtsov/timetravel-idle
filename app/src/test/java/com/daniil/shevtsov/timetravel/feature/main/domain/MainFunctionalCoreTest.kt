package com.daniil.shevtsov.timetravel.feature.main.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.actions.domain.action
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChange
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChanges
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.plot.domain.*
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.resource
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.timeMoment
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

    @Test
    fun `should apply resource changes of selected action`() {
        val resource = resource(id = ResourceId.Money, value = 100f)
        val action = action(
            id = ActionId(1L),
            resourceChanges = resourceChanges(
                resourceChange(id = resource.id, change = -25f)
            )
        )
        val initialState = gameState(
            actions = listOf(action),
            resources = listOf(resource),
        )

        val state = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectAction(id = action.id)
        )

        assertThat(state)
            .prop(GameState::resources)
            .extracting(Resource::id, Resource::value)
            .containsExactly(resource.id to 75f)
    }

    @Test
    fun `should restore moment in the past when travelling through time`() {
        val pastState = gameState(
            passedTime = PassedTime(Duration.milliseconds(5)),
        )
        val timeMoment = timeMoment()

        val currentState = gameState(
            passedTime = PassedTime(Duration.milliseconds(10))
        )

        val newState = mainFunctionalCore(
            state = currentState,
            viewAction = MainViewAction.TravelBackToMoment(id = timeMoment.id)
        )

        assertThat(newState)
            .prop(GameState::passedTime)
            .prop(PassedTime::value)
            .isEqualTo(pastState.passedTime)
    }

    private fun Assert<GameState>.extractingPlot() = prop(GameState::plot)
    private fun Assert<Plot>.extractingText() = prop(Plot::text)

}
