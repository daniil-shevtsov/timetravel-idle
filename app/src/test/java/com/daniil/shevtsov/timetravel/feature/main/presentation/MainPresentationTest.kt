package com.daniil.shevtsov.timetravel.feature.main.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.actions.domain.action
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChange
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChanges
import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.plot.domain.choice
import com.daniil.shevtsov.timetravel.feature.plot.domain.plot
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.resource
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.timeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import org.junit.jupiter.api.Test
import kotlin.time.Duration

class MainPresentationTest {

    @Test
    fun `should form correct initial view state`() {
        val choice = choice(id = ChoiceId(1L), text = "Choice")
        val plot = plot(
            text = "Text",
            choices = listOf(choice),
        )
        val timeMoment = timeMoment(
            id = TimeMomentId(1L),
            stateSnapshot = gameState(passedTime = PassedTime(Duration.seconds(2L)))
        )
        val viewState = mapMainViewState(
            state = gameState(
                plot = plot,
                passedTime = PassedTime(Duration.seconds(5L)),
                timeMoments = listOf(timeMoment)
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .all {
                prop(MainViewState.Content::plot)
                    .all {
                        prop(PlotViewState::text).isEqualTo(plot.text)
                        prop(PlotViewState::choices)
                            .extracting(ChoiceModel::id, ChoiceModel::text)
                            .containsExactly(choice.id to choice.text)
                    }
                prop(MainViewState.Content::resources)
                    .all {
                        prop(ResourcesViewState::passedTime)
                            .prop(ResourceModel::text)
                            .isEqualTo("5.00s")
                    }
                prop(MainViewState.Content::timeTravel)
                    .prop(TimeTravelViewState::moments)
                    .index(0)
                    .all {
                        prop(TimeMomentModel::id).isEqualTo(timeMoment.id)
                        prop(TimeMomentModel::time).isEqualTo(timeMoment.stateSnapshot.passedTime)
                    }
            }

    }

    @Test
    fun `should show only resources that you have`() {
        val viewState = mapMainViewState(
            state = gameState(
                resources = listOf(
                    resource(id = ResourceId.TimeCrystal, value = 0f),
                    resource(id = ResourceId.Money, value = 100f),
                ),
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::resources)
            .prop(ResourcesViewState::resources)
            .extracting(ResourceModel::id, ResourceModel::text)
            .containsExactly(ResourceId.Money to "100.0")
    }

    @Test
    fun `should show available actions`() {
        val action = action(
            id = ActionId(1L),
            resourceChanges = resourceChanges(
                resourceChange(
                    id = ResourceId.Money,
                    change = -50f
                )
            )
        )
        val viewState = mapMainViewState(
            state = gameState(
                actions = listOf(
                    action
                ),
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::actions)
            .extracting(ActionModel::id, ActionModel::title)
            .containsExactly(action.id to action.title)
    }

    @Test
    fun `should set moment parent correctly`() {
        val viewState = mapMainViewState(
            state = gameState(
                timeMoments = listOf(
                    timeMoment(id = TimeMomentId(0L), timelineParentId = null),
                    timeMoment(id = TimeMomentId(1L), timelineParentId = null),
                    timeMoment(id = TimeMomentId(2L), timelineParentId = null),
                    timeMoment(id = TimeMomentId(3L), timelineParentId = TimeMomentId(1L)),
                    timeMoment(id = TimeMomentId(4L), timelineParentId = TimeMomentId(1L)),
                )
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::timeTravel)
            .prop(TimeTravelViewState::moments)
            .extracting(TimeMomentModel::id, TimeMomentModel::momentParent)
            .containsExactly(
                TimeMomentId(0L) to null,
                TimeMomentId(1L) to TimeMomentId(0L),
                TimeMomentId(2L) to TimeMomentId(1L),
                TimeMomentId(3L) to TimeMomentId(1L),
                TimeMomentId(4L) to TimeMomentId(3L),
            )
    }

    @Test
    fun `should set moment parents correctly`() {
        val viewState = mapMainViewState(
            state = gameState(
                timeMoments = listOf(
                    timeMoment(id = TimeMomentId(0L), timelineParentId = null, parents = emptyList()),
                    timeMoment(id = TimeMomentId(1L), timelineParentId = null, parents = listOf(TimeMomentId(0L))),
                    timeMoment(id = TimeMomentId(2L), timelineParentId = null, parents = listOf(TimeMomentId(1L))),
                    timeMoment(id = TimeMomentId(3L), timelineParentId = TimeMomentId(1L), parents = listOf(TimeMomentId(1L))),
                    timeMoment(id = TimeMomentId(4L), timelineParentId = TimeMomentId(1L), parents = listOf(TimeMomentId(3L))),
                )
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::timeTravel)
            .prop(TimeTravelViewState::moments)
            .extracting(TimeMomentModel::id, TimeMomentModel::momentParents)
            .containsExactly(
                TimeMomentId(0L) to emptyList<TimeMomentId>(),
                TimeMomentId(1L) to listOf(TimeMomentId(0L)),
                TimeMomentId(2L) to listOf(TimeMomentId(1L)),
                TimeMomentId(3L) to listOf(TimeMomentId(1L)),
                TimeMomentId(4L) to listOf(TimeMomentId(3L)),
            )
    }

}
