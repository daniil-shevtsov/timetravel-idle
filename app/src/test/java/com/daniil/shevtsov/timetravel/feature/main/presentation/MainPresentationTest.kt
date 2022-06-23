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
                passedTime = PassedTime(Duration.ZERO),
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
            }

    }

}
