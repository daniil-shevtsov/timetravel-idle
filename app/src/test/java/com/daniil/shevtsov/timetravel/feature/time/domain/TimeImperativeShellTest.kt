package com.daniil.shevtsov.timetravel.feature.time.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds


class TimeImperativeShellTest {
    @Test
    fun `should emit time until given`() = runBlockingTest {
        val imperativeShell = TimeImperativeShell()

        imperativeShell.passedTime
            .test {
                imperativeShell.startEmitingTime(until = 4L.milliseconds, interval = 1L.milliseconds)

                assertThat(awaitItem()).isEqualTo(0L.milliseconds)
                assertThat(awaitItem()).isEqualTo(1L.milliseconds)
                assertThat(awaitItem()).isEqualTo(2L.milliseconds)
                assertThat(awaitItem()).isEqualTo(3L.milliseconds)
                assertThat(awaitItem()).isEqualTo(4L.milliseconds)
            }
    }
}
