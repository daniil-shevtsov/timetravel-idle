package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PrototypeTest {

    @Test
    fun `should time 0`() {
        test(
            time = 0f,
            duration = 3000f,
            nodes = listOf(0, 1, 2),
            expectedMomentIndex = 0,
            expectedSegmentFraction = 0f
        )
    }

    @Test
    fun `should time 3000`() {
        test(
            time = 3000f,
            duration = 3000f,
            nodes = listOf(0, 1, 2),
            expectedMomentIndex = 1,
            expectedSegmentFraction = 1f
        )
    }

    private fun test(
        time: Float,
        nodes: List<Int>,
        duration: Float,
        expectedMomentIndex: Int,
        expectedSegmentFraction: Float,
    ) {
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
                nodes = nodes,
            )
        ).isEqualTo(expectedSegmentFraction)
    }

    private fun calculateIndex(
        time: Float,
        duration: Float,
        nodes: List<Int>
    ): Int {
        return ((nodes.lastIndex - 1) * (time / duration)).toInt()
    }

    private fun calculateSegmentFraction(momentIndex: Int, time: Float, nodes: List<Int>): Float {
        return (momentIndex.toFloat()) / (nodes.lastIndex - 1)
    }

}
