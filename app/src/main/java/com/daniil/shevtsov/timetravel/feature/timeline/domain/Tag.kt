package com.daniil.shevtsov.timetravel.feature.timeline.domain

data class Tag(
    val id: TagId,
    val name: String,
)

@JvmInline
value class TagId(val id: Long)
