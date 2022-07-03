package com.daniil.shevtsov.timetravel.feature.tags.domain

sealed interface TagSubset {
    val tag: Tag
}

object Tags {
    enum class WorldState(override val tag: Tag) : TagSubset {
        OrdinaryWorld(
            tag = Tag(
                id = TagId(5L),
                name = "Ordinary World"
            )
        ),
        NuclearWasteland(
            tag = Tag(
                id = TagId(0L),
                name = "Nuclear Wasteland",
            )
        ),
        PostApocalypse(
            tag = Tag(
                id = TagId(1L),
                name = "Nuclear Wasteland",
            )
        ),
    }

    enum class Society(override val tag: Tag) : TagSubset {
        Functioning(
            tag = Tag(
                id = TagId(2L),
                name = "Normal functioning Society",
            )
        ),
        PostApocalyptic(
            tag = Tag(
                id = TagId(3L),
                name = "Post-apocalyptic battle for resources"
            )
        )
    }

    enum class PlayerJob(override val tag: Tag) : TagSubset {
        Employed(
            tag = Tag(
                id = TagId(4L),
                name = "Employed",
            )
        ),
    }
}

fun createAllTags(): List<Tag> = (
        Tags.WorldState.values().toList()
                + Tags.Society.values().toList()
                + Tags.PlayerJob.values().toList()
        )
    .map { it.tag }

fun createInitialPresentTags(): List<TagId> = listOf(
    Tags.WorldState.OrdinaryWorld.tag.id,
    Tags.Society.Functioning.tag.id,
    Tags.PlayerJob.Employed.tag.id,
)
