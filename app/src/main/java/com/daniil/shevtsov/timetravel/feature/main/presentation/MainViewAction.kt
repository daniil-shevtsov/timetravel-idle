package com.daniil.shevtsov.timetravel.feature.main.presentation

import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId

sealed class MainViewAction {
    object Init : MainViewAction()
    data class SelectChoice(val id: ChoiceId) : MainViewAction()
}
