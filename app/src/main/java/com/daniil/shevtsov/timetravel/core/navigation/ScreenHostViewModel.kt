package com.daniil.shevtsov.timetravel.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.drawerViewState
import com.daniil.shevtsov.timetravel.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.time.domain.TimeImperativeShell
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScreenHostViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
    private val timeImperativeShell: TimeImperativeShell,
) : ViewModel() {

    private val _state =
        MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private val viewActionFlow = MutableSharedFlow<ScreenViewAction>()

    init {
        viewActionFlow
            .onStart { emit(ScreenViewAction.Main(MainViewAction.Init)) }
            .onEach { viewAction ->
                val newState = screenFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction,
                )

                imperativeShell.updateState(newState)
            }
            .launchIn(viewModelScope)

        imperativeShell.observeState()
            .onEach { state ->
                _state.value = screenPresentationFunctionalCore(state = state)
            }
            .launchIn(viewModelScope)

        timeImperativeShell.passedTime
            .onEach {
                viewActionFlow.emit(ScreenViewAction.General(GeneralViewAction.Tick))
            }
            .launchIn(viewModelScope)
        viewModelScope.launch { timeImperativeShell.startEmitingTime() }
    }

    fun handleAction(action: ScreenViewAction) {
        viewModelScope.launch {
            viewActionFlow.emit(action)
        }
    }

    private fun createInitialState() = ScreenHostViewState(
        drawerState = drawerViewState(),
        contentState = ScreenContentViewState.Loading,
    )

}
