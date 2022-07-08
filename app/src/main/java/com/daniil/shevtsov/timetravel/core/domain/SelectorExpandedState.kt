package com.daniil.shevtsov.timetravel.core.domain


typealias SelectorExpandedStates = Map<SelectorKey, Boolean>

data class SelectorExpandedState(
    val key: SelectorKey,
    val isExpanded: Boolean,
)

fun selectorExpandedStates(
    vararg states: SelectorExpandedState = arrayOf(),
): SelectorExpandedStates = states.associate { it.key to it.isExpanded }

fun selectorExpandedState(key: SelectorKey, isExpanded: Boolean) =
    SelectorExpandedState(key, isExpanded)

@JvmInline
value class SelectorId(val raw: Long)
