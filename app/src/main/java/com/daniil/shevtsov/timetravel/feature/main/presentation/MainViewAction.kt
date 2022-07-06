package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.core.domain.SelectorKey
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.location.domain.LocationId
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.presentation.TransferDirection
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

sealed class MainViewAction {
    object Init : MainViewAction()
    data class SelectChoice(val id: ChoiceId) : MainViewAction()
    data class SelectAction(val id: ActionId) : MainViewAction()
    object RegisterTimePoint : MainViewAction()
    data class TravelBackToMoment(val id: TimeMomentId) : MainViewAction()
    data class ToggleExpanded(val key: SelectorKey) : MainViewAction()
    data class SelectLocation(val id: LocationId) : MainViewAction()
    data class TransferResource(val id: ResourceId, val direction: TransferDirection) : MainViewAction()
}
