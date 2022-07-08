package com.daniil.shevtsov.timetravel.feature.location.domain

data class Location(
    val id: LocationId,
    val title: String,
    val description: String,
)

fun location(
    id: LocationId = LocationId(0L),
    title: String = "",
    description: String = "",
) = Location(
    id = id,
    title = title,
    description = description,
)

@JvmInline
value class LocationId(val raw: Long)
