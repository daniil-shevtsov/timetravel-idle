package com.daniil.shevtsov.timetravel.feature.actions.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.prop
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.timetravel.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.resource
import org.junit.jupiter.api.Test

class ActionBehaviorTest {
    @Test
    fun `should apply resource changes of selected action`() {
        val resource = resource(id = ResourceId.Money, value = 100f)
        val action = action(
            id = ActionId(1L),
            resourceChanges = resourceChanges(
                resourceChange(id = resource.id, change = -25f)
            )
        )
        val initialState = gameState(
            actions = listOf(action),
            resources = listOf(resource),
        )

        val state = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectAction(id = action.id)
        )

        assertThat(state)
            .prop(GameState::resources)
            .extracting(Resource::id, Resource::value)
            .containsExactly(resource.id to 75f)
    }
}
