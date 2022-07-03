package com.daniil.shevtsov.timetravel.feature.actions.domain

import com.daniil.shevtsov.timetravel.feature.tags.domain.TagId
import kotlin.time.Duration

data class Action(
    val id: ActionId,
    val title: String,
    val resourceChanges: ResourceChanges,
    val requiredTime: Duration,
    val requiredTags: List<TagId>,
)

@JvmInline
value class ActionId(val value: Long)

fun action(
    id: ActionId = ActionId(0L),
    title: String = "",
    resourceChanges: ResourceChanges = resourceChanges(),
    requiredTime: Duration = Duration.ZERO,
    requiredTags: List<TagId> = emptyList(),
) = Action(
    id = id,
    title = title,
    resourceChanges = resourceChanges,
    requiredTime = requiredTime,
    requiredTags = requiredTags,
)
