package com.daniil.shevtsov.timetravel.feature.main.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
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
        val viewState = mapMainViewState(
            state = gameState(
                plot = plot,
                passedTime = PassedTime(Duration.seconds(5L)),
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

}
