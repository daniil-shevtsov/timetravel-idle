package com.daniil.shevtsov.timetravel.feature.time.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration


class TimeImperativeShellTest {
    @Test
    fun `should emit time until given`() = runBlockingTest {
        val imperativeShell = TimeImperativeShell()

        imperativeShell.passedTime
            .test {
                imperativeShell.startEmitingTime(until = Duration.milliseconds(4L), interval = Duration.milliseconds(1L))

                assertThat(awaitItem()).isEqualTo(Duration.milliseconds(0L))
                assertThat(awaitItem()).isEqualTo(Duration.milliseconds(1L))
                assertThat(awaitItem()).isEqualTo(Duration.milliseconds(2L))
                assertThat(awaitItem()).isEqualTo(Duration.milliseconds(3L))
                assertThat(awaitItem()).isEqualTo(Duration.milliseconds(4L))
            }
    }
}
