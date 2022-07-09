package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.timetravel.feature.timeline.view.calculateIndex
import com.daniil.shevtsov.timetravel.feature.timeline.view.calculateSegmentFraction
import com.daniil.shevtsov.timetravel.feature.timeline.view.formTimelinePath
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.timeMomentModel
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PrototypeTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("getData")
    fun `Addition works as expected`(testState: TestState) {
        test(testState)
    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                TestState(
                    time = 0f,
                    nodes = listOf(0, 1, 2),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 0f
                ),
                TestState(
                    time = 750f,
                    nodes = listOf(0, 1, 2),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 0.5f
                ),
                TestState(
                    time = 1500f,
                    nodes = listOf(0, 1, 2),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 1f
                ),
                TestState(
                    time = 2250f,
                    nodes = listOf(0, 1, 2),
                    duration = 3000f,
                    expectedMomentIndex = 1,
                    expectedSegmentFraction = 0.5f
                ),
                TestState(
                    time = 3000f,
                    nodes = listOf(0, 1, 2),
                    duration = 3000f,
                    expectedMomentIndex = 1,
                    expectedSegmentFraction = 1f
                ),
                TestState(
                    time = 0f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 0f
                ),
                TestState(
                    time = 500f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 0.5f
                ),
                TestState(
                    time = 1000f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 1.0f
                ),
                TestState(
                    time = 1500f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 1,
                    expectedSegmentFraction = 0.5f
                ),
                TestState(
                    time = 2000f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 1,
                    expectedSegmentFraction = 1.0f
                ),
            ).map { state ->
                Arguments.of(state)
            }
        }
    }

    data class TestState(
        val time: Float,
        val nodes: List<Int>,
        val duration: Float,
        val expectedMomentIndex: Int,
        val expectedSegmentFraction: Float,
    )

    private fun test(
        state: TestState,
    ) {
        with(state) {
            val momentIndex = calculateIndex(
                time = time,
                duration = duration,
                nodes = nodes,
            )
            assertThat(momentIndex).isEqualTo(expectedMomentIndex)

            assertThat(
                calculateSegmentFraction(
                    momentIndex = momentIndex,
                    time = time,
                    duration = duration,
                    nodes = nodes,
                )
            ).isEqualTo(expectedSegmentFraction)
        }
    }

    @Test
    fun `should form path for simple case`() {
        assertThat(
            formTimelinePath(
                moments = listOf(
                    timeMomentModel(
                        id = TimeMomentId(1L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(1L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(3L),
                        momentParents = listOf(TimeMomentId(2L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(4L),
                        momentParents = listOf(TimeMomentId(3L)),
                    ),
                ),
                start = TimeMomentId(1L),
                destination = TimeMomentId(4L),
            )
        ).containsExactly(
            TimeMomentId(1L),
            TimeMomentId(2L),
            TimeMomentId(3L),
            TimeMomentId(4L),
        )
    }

    @Test
    fun `should form path for complex case`() {
        assertThat(
            formTimelinePath(
                moments = listOf(
                    timeMomentModel(
                        id = TimeMomentId(1L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(1L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(3L),
                        momentParents = listOf(TimeMomentId(2L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(7L),
                        timelineParent = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(2L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(8L),
                        timelineParent = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(7L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(4L),
                        momentParents = listOf(TimeMomentId(3L)),
                    ),
                ),
                start = TimeMomentId(1L),
                destination = TimeMomentId(8L),
            )
        ).containsExactly(
            TimeMomentId(1L),
            TimeMomentId(2L),
            TimeMomentId(7L),
            TimeMomentId(8L),
        )
    }

    @Test
    @Disabled("This behavior is not needed until there is working timeline pruning")
    fun `should form path for the most compplex case`() {
        assertThat(
            formTimelinePath(
                moments = listOf(
                    timeMomentModel(
                        id = TimeMomentId(1L),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(1L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(3L),
                        momentParents = listOf(TimeMomentId(2L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(4L),
                        momentParents = listOf(TimeMomentId(3L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(5L),
                        momentParents = listOf(
                            TimeMomentId(4),
                            TimeMomentId(10),
                        )
                    ),
                    timeMomentModel(
                        id = TimeMomentId(6L),
                        momentParents = listOf(TimeMomentId(5L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(7L),
                        timelineParent = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(2L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(8L),
                        timelineParent = TimeMomentId(2L),
                        momentParents = listOf(TimeMomentId(7L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(9L),
                        timelineParent = TimeMomentId(7L),
                        momentParents = listOf(TimeMomentId(7L)),
                    ),
                    timeMomentModel(
                        id = TimeMomentId(10L),
                        timelineParent = TimeMomentId(7L),
                        momentParents = listOf(TimeMomentId(9L)),
                    ),
                ),
                start = TimeMomentId(8L),
                destination = TimeMomentId(6L),
            )
        ).containsExactly(
            TimeMomentId(8L),
            TimeMomentId(7L),
            TimeMomentId(9L),
            TimeMomentId(10L),
            TimeMomentId(5L),
            TimeMomentId(6L),
        )
    }


}
