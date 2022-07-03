package com.daniil.shevtsov.timetravel.feature.tags.domain

object Tags {
    val nuclearWasteland = Tag(
        id = TagId(0L),
        name = "Nuclear Wasteland",
    )
}

fun createAllTags() = listOf(
    Tags.nuclearWasteland,
)
