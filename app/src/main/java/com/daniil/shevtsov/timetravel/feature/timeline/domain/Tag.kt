package com.daniil.shevtsov.timetravel.feature.timeline.domain

data class Tag(
    val id: TagId,
    val name: String,
)

fun tag(
    id: TagId = TagId(0L),
    name: String = "",
) = Tag(
    id = id,
    name = name,
)

@JvmInline
value class TagId(val id: Long)
