package com.daniil.shevtsov.timetravel.feature.time.domain

import com.daniil.shevtsov.timetravel.core.di.AppScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import javax.inject.Inject
import kotlin.time.Duration

@AppScope
class TimeImperativeShell @Inject constructor() {
    fun startEmitingTime(
        until: Duration = Duration.INFINITE,
    ): Flow<Duration> {
        val interval = Duration.milliseconds(500L)
        return timerFlow(interval)
            .takeWhile { duration -> duration <= until }
            .map { duration ->
                Duration.milliseconds(duration.inWholeMilliseconds / interval.inWholeMilliseconds)
            }
    }

    private fun timerFlow(interval: Duration): Flow<Duration> = flow {
        var elapsedTime = Duration.ZERO
        while (true) {
            emit(elapsedTime)
            elapsedTime += interval
            delay(interval)
        }
    }
}
