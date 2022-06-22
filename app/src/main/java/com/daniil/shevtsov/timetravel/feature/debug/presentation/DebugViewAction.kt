package com.daniil.shevtsov.timetravel.feature.debug.presentation

sealed class DebugViewAction {
    data class JobSelected(val id: Long): DebugViewAction()
    data class SpeciesSelected(val id: Long) : DebugViewAction()
}
