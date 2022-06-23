package com.daniil.shevtsov.timetravel.feature.resources.presentation

data class ResourcesViewState(
    val passedTime: ResourceModel,
    val resources: List<ResourceModel>,
)
