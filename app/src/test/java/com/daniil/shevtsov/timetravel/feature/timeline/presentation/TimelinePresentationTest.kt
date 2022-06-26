package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import assertk.all
import assertk.assertThat
import assertk.assertions.*
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
            .all {
                prop(TimelineViewState::moments)
                    .extracting(Moment::position)
                    .containsExactly(
                        Offset(
                            x = 20f,
                            y = 20f,
                        )
                    )
                prop(TimelineViewState::lines)
                    .isEmpty()
            }
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
            .all {
                prop(TimelineViewState::moments)
                    .extracting(Moment::position)
                    .containsExactly(
                        Offset(
                            x = 20f,
                            y = 20f,
                        ),
                        Offset(
                            x = 36f,
                            y = 20f,
                        ),
                    )
                prop(TimelineViewState::lines)
                    .index(0)
                    .all {
                        prop(Line::start).isEqualTo(Offset(x = 20f, y = 20f))
                        prop(Line::end).isEqualTo(Offset(x = 36f, y = 20f))
                    }
            }
    }

    @Test
    fun `should draw timeline split`() {
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
                ),
                TimeMomentId(0L) to listOf(
                    timeMomentModel(id = TimeMomentId(2L)),
                    timeMomentModel(id = TimeMomentId(3L)),
                )
            ),
            sizes = sizes,
        )

        assertThat(models)
            .all {
                prop(TimelineViewState::moments)
                    .extracting(Moment::id, Moment::position)
                    .containsExactly(
                        TimeMomentId(0L) to Offset(
                            x = 20f,
                            y = 20f,
                        ),
                        TimeMomentId(1L) to Offset(
                            x = 36f,
                            y = 20f,
                        ),
                        TimeMomentId(2L) to Offset(
                            x = 40f,
                            y = 42f,
                        ),
                        TimeMomentId(3L) to Offset(
                            x = 56f,
                            y = 42f,
                        ),
                    )
                prop(TimelineViewState::lines)
                    .extracting(Line::endMomentId, Line::start, Line::end)
                    .containsExactly(
                        Triple(
                            TimeMomentId(1L),
                            Offset(x = 20f, y = 20f),
                            Offset(x = 36f, y = 20f)
                        ),
                        Triple(
                            TimeMomentId(2L),
                            Offset(x = 20f, y = 20f),
                            Offset(x = 40f, y = 42f)
                        ),
                        Triple(
                            TimeMomentId(3L),
                            Offset(x = 40f, y = 42f),
                            Offset(x = 56f, y = 42f)
                        ),
                    )
            }
    }

}
