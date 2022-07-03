package com.daniil.shevtsov.timetravel.feature.tags.domain

sealed interface TagSubset {
    val tag: Tag
}

object Tags {
    enum class WorldState(override val tag: Tag) : TagSubset {
        NuclearWasteland(
            tag = Tag(
                id = TagId(0L),
                name = "Nuclear Wasteland",
            )
        ),
        FunctioningSociety(
            tag = Tag(
                id = TagId(1L),
                name = "Functioning Society",
            )
        )
    }

    enum class PlayerJob(override val tag: Tag) : TagSubset {
        Employed(
            tag = Tag(
                id = TagId(3L),
                name = "Employed",
            )
        ),
    }
}

fun createAllTags(): List<Tag> =
    (Tags.WorldState.values().toList() + Tags.PlayerJob.values().toList())
        .map { it.tag }
