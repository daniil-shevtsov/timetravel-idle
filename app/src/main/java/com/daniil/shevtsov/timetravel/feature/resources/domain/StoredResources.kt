package com.daniil.shevtsov.timetravel.feature.resources.domain

data class StoredResource(
    val id: ResourceId,
    val current: ResourceValue,
    val max: ResourceValue,
)

fun storedResource(
    id: ResourceId = ResourceId.Money,
    current: ResourceValue = ResourceValue(0f),
    max: ResourceValue = ResourceValue(0f),
) = StoredResource(
    id = id,
    current = current,
    max = max,
)
