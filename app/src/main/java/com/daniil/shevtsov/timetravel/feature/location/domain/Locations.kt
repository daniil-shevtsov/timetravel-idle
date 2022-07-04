package com.daniil.shevtsov.timetravel.feature.location.domain

object Locations {
    val researchLab = location(
        id = LocationId(0L),
        title = "Research Lab",
    )
    val spaceOutsideTime = location(
        id = LocationId(1L),
        title = "Space outside Time"
    )
}

fun createAllLocations(): List<Location> = listOf(
    Locations.researchLab,
    Locations.spaceOutsideTime,
)
