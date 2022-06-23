package com.daniil.shevtsov.timetravel.core.navigation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.core.domain.balanceConfig
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import org.junit.jupiter.api.Test
import kotlin.time.Duration


internal class GeneralFunctionalCoreTest {
    @Test
    fun `should update passed time when ticked`() {
        val tickRate = Duration.milliseconds(5)
        val stateAfterOneTick = generalFunctionalCore(
            state = gameState(
                balanceConfig(tickRate = tickRate),
                passedTime = PassedTime(Duration.ZERO)
            ),
            viewAction = GeneralViewAction.Tick,
        )

        assertThat(stateAfterOneTick)
            .prop(GameState::passedTime)
            .prop(PassedTime::value)
            .isEqualTo(tickRate)

        val stateAfterTwoTicks = generalFunctionalCore(
            state = stateAfterOneTick,
            viewAction = GeneralViewAction.Tick,
        )

        assertThat(stateAfterTwoTicks)
            .prop(GameState::passedTime)
            .prop(PassedTime::value)
            .isEqualTo(tickRate * 2)
    }

    //TODO: Uncomment when gave several screens
//    @Test
//    fun `should replace current screen when opening another`() {
//        val state = generalFunctionalCore(
//            state = gameState(currentScreen = Screen.Main),
//            viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame)
//        )
//
//        assertThat(state)
//            .prop(GameState::currentScreen)
//            .isEqualTo(Screen.FinishedGame)
//    }
//
//    @Test
//    fun `should add screen to stack when opening it`() {
//        val state = generalFunctionalCore(
//            state = gameState(currentScreen = Screen.Main, screenStack = listOf(Screen.Main)),
//            viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame)
//        )
//
//        assertThat(state)
//            .prop(GameState::screenStack)
//            .containsExactly(Screen.Main, Screen.FinishedGame)
//    }
//
//    @Test
//    fun `should replace screen in stack if replace flag`() {
//        val state = generalFunctionalCore(
//            state = gameState(currentScreen = Screen.Main, screenStack = listOf(Screen.Main)),
//            viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame, shouldReplace = true)
//        )
//
//        assertThat(state)
//            .prop(GameState::screenStack)
//            .containsExactly(Screen.FinishedGame)
//    }
//
//    @Test
//    fun `should go back when back clicked`() {
//        val state = generalFunctionalCore(
//            state = gameState(
//                currentScreen = Screen.FinishedGame,
//                screenStack = listOf(Screen.Main, Screen.FinishedGame)
//            ),
//            viewAction = GeneralViewAction.Back
//        )
//
//        assertThat(state)
//            .prop(GameState::currentScreen)
//            .isEqualTo(Screen.Main)
//    }
//
//    @Test
//    fun `should remove last element from screen stack when back clicked`() {
//        val state = generalFunctionalCore(
//            state = gameState(
//                currentScreen = Screen.FinishedGame,
//                screenStack = listOf(Screen.Main, Screen.FinishedGame)
//            ),
//            viewAction = GeneralViewAction.Back
//        )
//
//        assertThat(state)
//            .prop(GameState::screenStack)
//            .containsExactly(Screen.Main)
//    }
//
//    @Test
//    fun `should do nothing on back click when only one screen in stack`() {
//        val state = generalFunctionalCore(
//            state = gameState(
//                currentScreen = Screen.Main,
//                screenStack = listOf(Screen.Main)
//            ),
//            viewAction = GeneralViewAction.Back
//        )
//
//        assertThat(state)
//            .all {
//                prop(GameState::currentScreen).isEqualTo(Screen.Main)
//                prop(GameState::screenStack).containsExactly(Screen.Main)
//            }
//    }

}
