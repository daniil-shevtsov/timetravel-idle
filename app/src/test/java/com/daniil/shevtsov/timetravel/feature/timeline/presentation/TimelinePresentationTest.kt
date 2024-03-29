package com.daniil.shevtsov.timetravel.feature.timeline.presentation

import androidx.compose.ui.geometry.Offset
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import org.junit.jupiter.api.Test

class TimelinePresentationTest {

    val sizes = TimelineSizes(
        canvasPadding = 14f,
        point = 12f,
        segment = 4f,
        timelineSplitOffset = Offset(
            x = 2f,
            y = 22f,
        ),
    )

    @Test
    fun `should draw one point`() {
        val models = timelinePresentation(
            allMoments = listOf(timeMomentModel()),
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
        val models = timelinePresentation(
            allMoments = listOf(
                timeMomentModel(
                    id = TimeMomentId(0L),
                    timelineParent = null,
                    momentParents = emptyList()
                ),
                timeMomentModel(
                    id = TimeMomentId(1L),
                    timelineParent = null,
                    momentParents = listOf(TimeMomentId(0L))
                ),
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
        val models = timelinePresentation(
            allMoments = listOf(
                timeMomentModel(
                    id = TimeMomentId(0L),
                    timelineParent = null,
                    momentParents = emptyList()
                ),
                timeMomentModel(
                    id = TimeMomentId(1L),
                    timelineParent = null,
                    momentParents = listOf(TimeMomentId(0L))
                ),
                timeMomentModel(
                    id = TimeMomentId(2L),
                    timelineParent = TimeMomentId(0L),
                    momentParents = listOf(TimeMomentId(0L))
                ),
                timeMomentModel(
                    id = TimeMomentId(3L),
                    timelineParent = TimeMomentId(0L),
                    momentParents = listOf(TimeMomentId(2L))
                ),
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
                            x = 28f,
                            y = 42f,
                        ),
                        TimeMomentId(3L) to Offset(
                            x = 44f,
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
                            Offset(x = 28f, y = 42f)
                        ),
                        Triple(
                            TimeMomentId(3L),
                            Offset(x = 28f, y = 42f),
                            Offset(x = 44f, y = 42f)
                        ),
                    )
            }
    }

    @Test
    fun `should draw two timeline splits`() {
        val models = timelinePresentation(
            allMoments = listOf(
                timeMomentModel(
                    id = TimeMomentId(0L),
                    timelineParent = null,
                    momentParents = emptyList()
                ),
                timeMomentModel(
                    id = TimeMomentId(1L),
                    timelineParent = null,
                    momentParents = listOf(TimeMomentId(0L))
                ),
                timeMomentModel(
                    id = TimeMomentId(2L),
                    timelineParent = null,
                    momentParents = listOf(TimeMomentId(1L))
                ),
                timeMomentModel(
                    id = TimeMomentId(3L),
                    timelineParent = TimeMomentId(1L),
                    momentParents = listOf(TimeMomentId(1L))
                ),
                timeMomentModel(
                    id = TimeMomentId(4L),
                    timelineParent = TimeMomentId(1L),
                    momentParents = listOf(TimeMomentId(3L))
                ),
                timeMomentModel(
                    id = TimeMomentId(5L),
                    timelineParent = TimeMomentId(3L),
                    momentParents = listOf(TimeMomentId(3L))
                ),
                timeMomentModel(
                    id = TimeMomentId(6L),
                    timelineParent = TimeMomentId(3L),
                    momentParents = listOf(TimeMomentId(5L))
                ),
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
                            x = 52f,
                            y = 20f,
                        ),
                        TimeMomentId(3L) to Offset(
                            x = 44f,
                            y = 42f,
                        ),
                        TimeMomentId(4L) to Offset(
                            x = 60f,
                            y = 42f,
                        ),
                        TimeMomentId(5L) to Offset(
                            x = 52f,
                            y = 64f,
                        ),
                        TimeMomentId(6L) to Offset(
                            x = 68f,
                            y = 64f,
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
                            Offset(x = 36f, y = 20f),
                            Offset(x = 52f, y = 20f)
                        ),
                        Triple(
                            TimeMomentId(3L),
                            Offset(x = 36f, y = 20f),
                            Offset(x = 44f, y = 42f)
                        ),
                        Triple(
                            TimeMomentId(4L),
                            Offset(x = 44f, y = 42f),
                            Offset(x = 60f, y = 42f)
                        ),
                        Triple(
                            TimeMomentId(5L),
                            Offset(x = 44f, y = 42f),
                            Offset(x = 52f, y = 64f)
                        ),
                        Triple(
                            TimeMomentId(6L),
                            Offset(x = 52f, y = 64f),
                            Offset(x = 68f, y = 64f)
                        ),
                    )
            }
    }

    @Test
    fun `should connect timeline into main`() {
        val models = timelinePresentation(
            allMoments = listOf(
                timeMomentModel(
                    id = TimeMomentId(0L),
                    timelineParent = null,
                    momentParents = emptyList()
                ),
                timeMomentModel(
                    id = TimeMomentId(1L),
                    timelineParent = null,
                    momentParents = listOf(TimeMomentId(0L))
                ),
                timeMomentModel(
                    id = TimeMomentId(3L),
                    timelineParent = null,
                    momentParents = listOf(
                        TimeMomentId(1L),
                        TimeMomentId(2L)
                    )
                ),
                timeMomentModel(
                    id = TimeMomentId(2L),
                    timelineParent = TimeMomentId(0L),
                    momentParents = listOf(TimeMomentId(0L))
                ),
            ),
            sizes = sizes,
        )

//        canvasPadding = 14f,
//        point = 12f,
//        segment = 4f,
//        timelineSplitOffset = Offset(
//            x = 2f,
//            y = 22f,
//        ),
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
                            x = 28f,
                            y = 42f,
                        ),
                        TimeMomentId(3L) to Offset(
                            x = 52f,
                            y = 20f,
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
                            Offset(x = 28f, y = 42f)
                        ),
                        Triple(
                            TimeMomentId(3L),
                            Offset(x = 36f, y = 20f),
                            Offset(x = 52f, y = 20f)
                        ),
                        Triple(
                            TimeMomentId(3L),
                            Offset(x = 28f, y = 42f),
                            Offset(x = 52f, y = 20f)
                        ),
                    )
            }
    }

}
