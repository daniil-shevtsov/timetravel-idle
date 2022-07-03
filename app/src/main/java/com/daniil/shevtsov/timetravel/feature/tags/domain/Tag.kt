package com.daniil.shevtsov.timetravel.feature.tags.domain

data class Tag(
    val id: TagId,
    val name: String,
)

typealias TagChanges = Map<TagId, Change>

fun tagChanges(
    vararg changes: Pair<TagId, Change> = arrayOf(),
): TagChanges = changes.toMap()

fun tagsToAdd(tags: List<Tag>): TagChanges = tagChanges(
    changes = tags.map { it.id to Change.Add }.toTypedArray()
)

enum class Change {
    Add,
    Remove,
}

fun tag(
    id: TagId = TagId(0L),
    name: String = "",
) = Tag(
    id = id,
    name = name,
)

@JvmInline
value class TagId(val id: Long)
