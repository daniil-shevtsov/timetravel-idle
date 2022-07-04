package com.daniil.shevtsov.timetravel.core.ui.widgets.selector

data class SelectorModel(
    val id: SelectorId,
    val title: String,
)

@JvmInline
value class SelectorId(val id: Long)
