package com.daniil.shevtsov.timetravel.feature.actions.domain

import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import kotlin.time.Duration

fun createInitialActions() = listOf(
    action(
        id = ActionId(0L),
        title = "Grow time crystal",
        requiredTime = Duration.seconds(5L),
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.TimeCrystal, change = 1f),
            resourceChange(id = ResourceId.Money, change = -25f),
        )
    ),
    action(
        id = ActionId(1L),
        title = "Earn money",
        requiredTime = Duration.seconds(5L),
        resourceChanges = resourceChanges(
            resourceChange(id = ResourceId.Money, change = 100f)
        )
    ),
)
