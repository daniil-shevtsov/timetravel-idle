package com.daniil.shevtsov.timetravel.application

import com.daniil.shevtsov.timetravel.feature.main.data.MainImperativeShell
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration

class TimeTravelGameViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    fun onStart() {

    }

    fun onCleared() {
        scope.cancel()
    }

}
