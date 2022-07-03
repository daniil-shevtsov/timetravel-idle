package com.daniil.shevtsov.timetravel.feature.tags.domain

data class Tag(
    val id: TagId,
    val name: String,
)

typealias TagChanges = Map<TagId, Change>
typealias TagChange = Pair<TagId, Change>

fun tagChanges(
    vararg changes: TagChange = arrayOf(),
): TagChanges = changes.toMap()

fun tagChange(
    id: TagId,
    change: Change
): TagChange = id to change

fun tagsToAdd(tags: List<TagId>): TagChanges = tagChanges(
    changes = tags.map { id -> tagChange(id = id, change = Change.Add) }.toTypedArray()
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
