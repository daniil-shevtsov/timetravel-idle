package com.daniil.shevtsov.timetravel.core.ui.widgets.selector

import com.daniil.shevtsov.timetravel.core.domain.SelectorId

data class SelectorModel(
    val id: SelectorId,
    val title: String,
)

fun selectorModel(
    id: SelectorId = SelectorId(0L),
    title: String = "",
) = SelectorModel(
    id = id,
    title = title,
)

