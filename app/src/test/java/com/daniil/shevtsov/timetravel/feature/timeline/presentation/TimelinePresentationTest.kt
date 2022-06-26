package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import org.junit.jupiter.api.Test

class TimelinePresentationTest {

    @Test
    fun `should draw one point`() {
        val sizes = TimelineSizes(
            canvasPadding = 14f,
            point = 12f,
            segment = 16f,
        )
        val models = timelinePresentation(
            allTimelines = mapOf(
                null to listOf(
                    timeMomentModel(),
                )
            ),
            sizes = sizes,
        )

        assertThat(models)
            .prop(TimelineViewState::moments)
            .extracting(Moment::position)
            .containsExactly(
                Offset(
                    x = 20f,
                    y = 20f,
                )
            )
    }

    @Test
    fun `should draw two points on the same timeline`() {
        val sizes = TimelineSizes(
            canvasPadding = 14f,
            point = 12f,
            segment = 16f,
        )
        val models = timelinePresentation(
            allTimelines = mapOf(
                null to listOf(
                    timeMomentModel(id = TimeMomentId(0L)),
                    timeMomentModel(id = TimeMomentId(1L)),
                )
            ),
            sizes = sizes,
        )

        assertThat(models)
            .prop(TimelineViewState::moments)
            .extracting(Moment::position)
            .containsExactly(
                Offset(
                    x = 20f,
                    y = 14f,
                ),
                Offset(
                    x = 36f,
                    y = 14f,
                ),
            )
    }

}
