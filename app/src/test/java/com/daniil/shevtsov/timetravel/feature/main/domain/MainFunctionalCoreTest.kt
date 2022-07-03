package com.daniil.shevtsov.timetravel.feature.main.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.plot.domain.*
import com.daniil.shevtsov.timetravel.feature.tags.domain.TagId
import com.daniil.shevtsov.timetravel.feature.tags.domain.tag
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.timeMoment
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


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
    fun `should add tag when plot selected`() {
        val tagToAdd = tag(id = TagId(1L), name = "tag")
        val plotToSelect = plot(id = PlotId(1L), tagsToAdd = listOf(tagToAdd.id))
        val choiceToSelect = choice(
            id = ChoiceId(1L),
            destinationPlotId = plotToSelect.id,
        )
        val initialPlot = plot(
            id = PlotId(0L),
            choices = listOf(
                choiceToSelect
            )
        )
        val state = mainFunctionalCore(
            state = gameState(
                plot = initialPlot,
                plots = listOf(
                    initialPlot,
                    plotToSelect,
                ),
                allTags = listOf(tagToAdd),
            ),
            viewAction = MainViewAction.SelectChoice(id = choiceToSelect.id)
        )

        assertThat(state)
            .prop(GameState::presentTags)
            .containsExactly(tagToAdd.id)
    }

    @Test
    fun `should save gamestatesnapshot when register time point clicked`() {
        val initialState = gameState()
        val state = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.RegisterTimePoint,
        )

        assertThat(state)
            .prop(GameState::timeMoments)
            .index(0)
            .prop(TimeMoment::stateSnapshot)
            .isEqualTo(initialState)
    }

    @Test
    fun `should restore moment in the past when travelling through time`() {
        val pastState = gameState(
            passedTime = PassedTime(5.milliseconds),
        )

        val timeMoment = timeMoment(id = TimeMomentId(1L), stateSnapshot = pastState)
        val currentState = gameState(
            passedTime = PassedTime(10.milliseconds),
            timeMoments = listOf(timeMoment),
        )

        val newState = mainFunctionalCore(
            state = currentState,
            viewAction = MainViewAction.TravelBackToMoment(id = timeMoment.id)
        )

        assertThat(newState)
            .all {
                prop(GameState::passedTime)
                    .isEqualTo(pastState.passedTime)

                prop(GameState::currentMomentId)
                    .isEqualTo(timeMoment.id)
            }

    }

    @Test
    fun `should keep time moments when travelling through time`() {
        val pastState = gameState(
            passedTime = PassedTime(5.milliseconds),
        )

        val timeMoment = timeMoment(id = TimeMomentId(1L), stateSnapshot = pastState)
        val futureState = gameState(
            passedTime = PassedTime(10.milliseconds),
            timeMoments = listOf(timeMoment),
        )

        val newState = mainFunctionalCore(
            state = futureState,
            viewAction = MainViewAction.TravelBackToMoment(id = timeMoment.id)
        )

        assertThat(newState)
            .prop(GameState::timeMoments)
            .extracting(TimeMoment::id)
            .containsExactly(
                timeMoment.id,
            )
    }

    @Test
    fun `should split timeline if registering moment in the past`() {
        val pastState = gameState(
            passedTime = PassedTime(5.milliseconds),
        )

        val timeMoment = timeMoment(id = TimeMomentId(1L), stateSnapshot = pastState)
        val futureMoment = timeMoment(id = TimeMomentId(2L), stateSnapshot = pastState)
        val currentState = gameState(
            passedTime = PassedTime(10.milliseconds),
            timeMoments = listOf(timeMoment, futureMoment),
            lastTimeMomentId = timeMoment.id,
        )
        val expectedNewMoment = timeMoment(id = TimeMomentId(3L))

        val newState = mainFunctionalCore(
            state = currentState,
            viewAction = MainViewAction.RegisterTimePoint,
        )

        assertThat(newState)
            .prop(GameState::timeMoments)
            .extracting(TimeMoment::id, TimeMoment::timelineParentId)
            .containsExactly(
                timeMoment.id to null,
                futureMoment.id to null,
                expectedNewMoment.id to timeMoment.id,
            )
        assertThat(newState)
            .prop(GameState::timeMoments)
            .extracting(TimeMoment::id, TimeMoment::parents)
            .containsExactly(
                timeMoment.id to emptyList<TimeMomentId>(),
                futureMoment.id to emptyList<TimeMomentId>(),
                expectedNewMoment.id to listOf(timeMoment.id),
            )
    }

    @Test
    fun `should continue timeline when got back and registering time moment`() {
        val mainTimelineMoment1 = timeMoment(id = TimeMomentId(1L))
        val mainTimelineMoment2 =
            timeMoment(id = TimeMomentId(2L), parents = listOf(mainTimelineMoment1.id))
        val splitTimelineMoment =
            timeMoment(
                id = TimeMomentId(3L),
                timelineParentId = mainTimelineMoment1.id,
                parents = listOf(mainTimelineMoment1.id)
            )
        val expectedNewMomentId = TimeMomentId(4L)
        val currentState = gameState(
            passedTime = PassedTime(10.milliseconds),
            timeMoments = listOf(mainTimelineMoment1, mainTimelineMoment2, splitTimelineMoment),
            lastTimeMomentId = mainTimelineMoment2.id,
        )

        val stateAfterTravellingBack = mainFunctionalCore(
            state = currentState,
            viewAction = MainViewAction.TravelBackToMoment(id = mainTimelineMoment2.id),
        )

        val stateAfterRegisteringNewPoint =
            mainFunctionalCore(
                state = stateAfterTravellingBack,
                viewAction = MainViewAction.RegisterTimePoint,
            )

        assertThat(stateAfterRegisteringNewPoint)
            .all {
                prop(GameState::timeMoments)
                    .extracting(TimeMoment::id, TimeMoment::timelineParentId)
                    .contains(expectedNewMomentId to null)
                prop(GameState::timeMoments)
                    .extracting(TimeMoment::id, TimeMoment::parents)
                    .contains(expectedNewMomentId to listOf(mainTimelineMoment2.id))
                prop(GameState::currentMomentId).isEqualTo(expectedNewMomentId)
            }
    }

    private fun Assert<GameState>.extractingPlot() = prop(GameState::plot)
    private fun Assert<Plot>.extractingText() = prop(Plot::text)

}
