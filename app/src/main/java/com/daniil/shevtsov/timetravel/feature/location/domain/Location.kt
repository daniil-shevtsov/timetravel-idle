package com.daniil.shevtsov.timetravel.feature.location.domain

data class Location(
    val id: LocationId,
    val title: String,
)

fun location(
    id: LocationId = LocationId(0L),
    title: String = "",
) = Location(
    id = id,
    title = title,
)

@JvmInline
value class LocationId(val id: Long)
