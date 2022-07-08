package com.daniil.shevtsov.timetravel.feature.location.domain

object Locations {
    val researchLab = location(
        id = LocationId(0L),
        title = "Research Lab",
        description = "The lab where you have been tinkering with the time machine"
    )
    val spaceOutsideTime = location(
        id = LocationId(1L),
        title = "Space outside Time",
        description = "A space that is not accessible by anyone except you"
    )
}

fun createAllLocations(): List<Location> = listOf(
    Locations.researchLab,
    Locations.spaceOutsideTime,
)
