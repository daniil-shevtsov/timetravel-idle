package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId

sealed class MainViewAction {
    object Init : MainViewAction()
    data class SelectChoice(val id: ChoiceId) : MainViewAction()
    data class SelectAction(val id: ActionId) : MainViewAction()
    data class TravelBackToMoment(val id: TimeMomentId): MainViewAction()
}
