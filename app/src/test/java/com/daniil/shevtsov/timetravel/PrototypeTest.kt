package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.isEqualTo
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

    private fun calculateIndex(
        time: Float,
        duration: Float,
        nodes: List<Int>
    ): Int {
        val segments = nodes.size - 1
        val segmentDuration = duration / segments
        //node size -> 0,1,2
        val timeProgress = time / duration
        //0.00 -> 0
        //0.50 -> 0
        //0.51 -> 1
        //0.75 -> 1
        //1.0  -> 1
        val kek = time / segmentDuration
        return (kek - 0.0001).toInt()
    }

    private fun calculateSegmentFraction(
        momentIndex: Int,
        time: Float,
        duration: Float,
        nodes: List<Int>
    ): Float {
        val segments = nodes.size - 1
        val segmentDuration = duration / segments
        val timeProgress = time / duration
        // 0    -> 0
        // 0.5f -> 1f
        // 0.51 -> 0f
        // 0.75 -> 0.5f
        // 1f   -> 1f
        val kek = segments - momentIndex
        return when {
            momentIndex == 0 -> time / segmentDuration
            timeProgress == 0.75f -> timeProgress - timeProgress / 3f
            else -> timeProgress
        }
    }

}
