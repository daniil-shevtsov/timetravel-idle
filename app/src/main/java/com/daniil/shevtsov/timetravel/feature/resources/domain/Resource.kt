package com.daniil.shevtsov.timetravel.feature.resources.domain

data class Resource(
    val id: ResourceId,
    val name: String,
    val value: Float,
)

enum class ResourceId {
    Time,
    TimeCrystal,
    Money,
    NuclearWaste,
    Caps,
}

fun resource(
    id: ResourceId = ResourceId.TimeCrystal,
    name: String = "",
    value: Float = 0f,
) = Resource(
    id = id,
    name = name,
    value = value,
)
