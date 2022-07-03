package com.daniil.shevtsov.timetravel.feature.actions.domain

import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.tags.domain.Tags
import kotlin.time.Duration.Companion.seconds

fun createInitialActions() = listOf(
    action(
        id = ActionId(0L),
        title = "Grow time crystal",
        requiredTime = 5L.seconds,
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.TimeCrystal, change = 1f),
            resourceChange(id = ResourceId.Money, change = -25f),
        )
    ),
    action(
        id = ActionId(1L),
        title = "Earn money",
        requiredTime = 5L.seconds,
        requiredTags = listOf(
            Tags.Society.Functioning.tag.id,
            Tags.PlayerJob.Employed.tag.id,
        ),
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.Money, change = 100f)
        )
    ),
    action(
        id = ActionId(2L),
        title = "Pass time",
        requiredTime = 5L.seconds,
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.Time, change = 100f)
        )
    ),
    action(
        id = ActionId(3L),
        title = "Gather nuclear waste",
        requiredTime = 5L.seconds,
        requiredTags = listOf(Tags.WorldState.NuclearWasteland.tag.id),
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.NuclearWaste, change = 1f),
        )
    ),
)
