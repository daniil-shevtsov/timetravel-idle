package com.daniil.shevtsov.timetravel.feature.actions.presentation

import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId

data class ActionModel(
    val id: ActionId,
    val title: String,
)
