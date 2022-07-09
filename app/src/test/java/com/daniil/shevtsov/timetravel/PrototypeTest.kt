package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PrototypeTest {

    @Test
    fun `should time 0`() {
        assertThat(
            calculateSegmentFraction(
                momentIndex = 0,
                time = 0f,
                nodes = listOf(0, 1, 2),
            )
        ).isEqualTo(0f)
    }

    @Test
    fun `should 3000`() {
        assertThat(
            calculateSegmentFraction(
                momentIndex = 1,
                time = 3000f,
                nodes = listOf(0, 1, 2),
            )
        ).isEqualTo(1f)
    }

    private fun calculateSegmentFraction(momentIndex: Int, time: Float, nodes: List<Int>): Float {
        return (momentIndex.toFloat()) / (nodes.lastIndex - 1)
    }

}
