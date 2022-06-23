package com.daniil.shevtsov.timetravel.feature.actions.domain

import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId

typealias ResourceChanges = Map<ResourceId, Float>

data class ResourceChange(
    val id: ResourceId,
    val change: Float,
)

fun resourceChanges(
    vararg changes: ResourceChange = arrayOf(),
): ResourceChanges = changes.associate { it.id to it.change }

fun resourceChange(id: ResourceId, change: Float) = ResourceChange(id, change)
