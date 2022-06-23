package com.daniil.shevtsov.timetravel.feature.timetravel.domain

data class TimeMoment(
    val id: TimeMomentId,
)

@JvmInline
value class TimeMomentId(val value: Long)

fun timeMoment(
    id: TimeMomentId = TimeMomentId(0L),
) = TimeMoment(
    id = id,
)
