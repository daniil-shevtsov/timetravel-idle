package com.daniil.shevtsov.timetravel.feature.actions.domain

import kotlin.time.Duration

data class Action(
    val id: ActionId,
    val title: String,
    val resourceChanges: ResourceChanges,
    val requiredTime: Duration,
)

@JvmInline
value class ActionId(val value: Long)

fun action(
    id: ActionId = ActionId(0L),
    title: String = "",
    resourceChanges: ResourceChanges = resourceChanges(),
    requiredTime: Duration = Duration.ZERO,
) = Action(
    id = id,
    title = title,
    resourceChanges = resourceChanges,
    requiredTime = requiredTime,
)
