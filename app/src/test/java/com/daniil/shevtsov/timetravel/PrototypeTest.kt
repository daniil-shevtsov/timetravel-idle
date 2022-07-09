package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.timetravel.feature.timeline.view.calculateIndex
import com.daniil.shevtsov.timetravel.feature.timeline.view.calculateSegmentFraction
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
                    time = 1499.99999f,
                    nodes = listOf(0, 1, 2, 3),
                    duration = 3000f,
                    expectedMomentIndex = 0,
                    expectedSegmentFraction = 0.49f
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

//    @Test
//    fun `should`() {
//        assertThat(formTimelinePath(
//            timelineViewState = TimelineViewState(
//                moments = emptyList(),
//                lines = listOf(
//                    Line(
//                        endMomentId = TimeMomentId(0L),
//
//                    )
//                )
//            )
//        ))
//        val nodePath = listOf(
//            TimeMomentId(1L),
//            TimeMomentId(2L),
//            TimeMomentId(7L),
//            TimeMomentId(8L),
//        )
//    }



}
