package com.daniil.shevtsov.timetravel.feature.info.presentation

data class InfoViewState(
    val presentTags: List<String>,
)

fun infoViewState(
    presentTags: List<String> = emptyList(),
) = InfoViewState(
    presentTags = presentTags,
)
