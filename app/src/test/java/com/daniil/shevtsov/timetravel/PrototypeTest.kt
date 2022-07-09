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
    fun `should time 750`() {
        test(
            time = 750f,
            duration = 3000f,
            nodes = listOf(0, 1, 2),
            expectedMomentIndex = 0,
            expectedSegmentFraction = 0.5f
        )
    }

    @Test
    fun `should time 1500`() {
        test(
            time = 1500f,
            duration = 3000f,
            nodes = listOf(0, 1, 2),
            expectedMomentIndex = 0,
            expectedSegmentFraction = 1f
        )
    }

    @Test
    fun `should time 2250`() {
        test(
            time = 2250f,
            duration = 3000f,
            nodes = listOf(0, 1, 2),
            expectedMomentIndex = 1,
            expectedSegmentFraction = 0.5f
        )
    }

//    @Test
//    fun `should time 3000`() {
//        test(
//            time = 3000f,
//            duration = 3000f,
//            nodes = listOf(0, 1, 2),
//            expectedMomentIndex = 1,
//            expectedSegmentFraction = 1f
//        )
//    }

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
                duration = duration,
                nodes = nodes,
            )
        ).isEqualTo(expectedSegmentFraction)
    }

    private fun calculateIndex(
        time: Float,
        duration: Float,
        nodes: List<Int>
    ): Int {
        //node size -> 0,1,2
        val timeProgress = time / duration
        //0.00 -> 0
        //0.50 -> 0
        //0.51 -> 1
        //0.75 -> 1
        //1.0  -> 1
        return when {
            timeProgress <= 0.50f -> 0
            else -> 1
        }
    }

    private fun calculateSegmentFraction(
        momentIndex: Int,
        time: Float,
        duration: Float,
        nodes: List<Int>
    ): Float {
        val segments = nodes.size - 1
        val timeProgress = time / duration
        // 0    -> 0
        // 0.5f -> 1f
        // 0.51 -> 0f
        // 0.75 -> 0.5f
        // 1f   -> 1f

        return when(val kek = segments - momentIndex) {
            2 -> timeProgress * segments
            else -> timeProgress - timeProgress / 3f
        }
    }

}
