package com.daniil.shevtsov.timetravel.feature.time.domain

import com.daniil.shevtsov.timetravel.core.di.AppScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@AppScope
class TimeImperativeShell @Inject constructor() {
    private val _passedTime = MutableStateFlow(Duration.ZERO)
    val passedTime: Flow<Duration> = _passedTime.asStateFlow()

    suspend fun startEmitingTime(
        until: Duration = Duration.INFINITE,
        interval: Duration = 500L.milliseconds,
    ) {
        timerFlow(interval)
            .takeWhile { duration -> duration <= until }
            .map { duration ->
                (duration.inWholeMilliseconds / interval.inWholeMilliseconds).milliseconds
            }
            .collect { passed ->
                _passedTime.emit(passed)
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
