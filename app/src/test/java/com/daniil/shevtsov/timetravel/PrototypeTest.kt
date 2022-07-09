package com.daniil.shevtsov.timetravel

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PrototypeTest {

    private val indices = listOf(
        0, 1, 2, 3, 4, 5
    )

    @Test
    fun `should time 0`() {
        val time = 0f
        val start = calculateSegmentFraction(momentIndex = 0, time = time)

        assertThat(start).isEqualTo(0f)
    }

    @Test
    fun `should 3000`() {
        val time = 3000f
        val start = calculateSegmentFraction(momentIndex = 4, time = time)

        assertThat(start).isEqualTo(1f)
    }

    private fun calculateSegmentFraction(momentIndex: Int, time: Float): Float {
        return (momentIndex.toFloat()) / (indices.size - 2)
    }

}
