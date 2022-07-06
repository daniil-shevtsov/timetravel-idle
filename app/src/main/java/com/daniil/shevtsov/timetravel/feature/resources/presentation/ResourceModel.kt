package com.daniil.shevtsov.timetravel.feature.resources.presentation

import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId

data class ResourceModel(
    val id: ResourceId,
    val title: String,
    val text: String,
    val stored: String? = null,
)
