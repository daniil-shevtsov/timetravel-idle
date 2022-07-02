package com.daniil.shevtsov.timetravel.feature.info.presentation

import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState

fun infoPresentation(state: GameState): InfoViewState {

    return InfoViewState(
        presentTags = state.presentTags
            .map { tagId -> state.allTags.find { tag -> tag.id == tagId }?.name ?: "unknown" }
    )
}
