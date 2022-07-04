package com.daniil.shevtsov.timetravel.core.ui.widgets.selector

data class SelectorViewState(
    val items: List<SelectorModel>,
    val selectedItem: SelectorModel,
    val isExpanded: Boolean,
)
