package com.daniil.shevtsov.timetravel.feature.main.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.timetravel.core.domain.SelectorId
import com.daniil.shevtsov.timetravel.core.domain.SelectorKey
import com.daniil.shevtsov.timetravel.core.domain.selectorExpandedState
import com.daniil.shevtsov.timetravel.core.domain.selectorExpandedStates
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorModel
import com.daniil.shevtsov.timetravel.core.ui.widgets.selector.SelectorViewState
import com.daniil.shevtsov.timetravel.feature.actions.domain.ActionId
import com.daniil.shevtsov.timetravel.feature.actions.domain.action
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChange
import com.daniil.shevtsov.timetravel.feature.actions.domain.resourceChanges
import com.daniil.shevtsov.timetravel.feature.actions.presentation.ActionModel
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.gameState
import com.daniil.shevtsov.timetravel.feature.location.domain.Location
import com.daniil.shevtsov.timetravel.feature.location.domain.LocationId
import com.daniil.shevtsov.timetravel.feature.location.domain.location
import com.daniil.shevtsov.timetravel.feature.location.presentation.LocationViewState
import com.daniil.shevtsov.timetravel.feature.plot.domain.ChoiceId
import com.daniil.shevtsov.timetravel.feature.plot.domain.choice
import com.daniil.shevtsov.timetravel.feature.plot.domain.plot
import com.daniil.shevtsov.timetravel.feature.plot.presentation.ChoiceModel
import com.daniil.shevtsov.timetravel.feature.plot.presentation.PlotViewState
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceValue
import com.daniil.shevtsov.timetravel.feature.resources.domain.resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.storedResource
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.tags.domain.TagId
import com.daniil.shevtsov.timetravel.feature.tags.domain.tag
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.TimeMomentId
import com.daniil.shevtsov.timetravel.feature.timetravel.domain.timeMoment
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeMomentModel
import com.daniil.shevtsov.timetravel.feature.timetravel.presentation.TimeTravelViewState
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class MainPresentationTest {

    @Test
    fun `should form correct initial view state`() {
        val choice = choice(id = ChoiceId(1L), text = "Choice")
        val plot = plot(
            text = "Text",
            choices = listOf(choice),
        )
        val timeMoment = timeMoment(
            id = TimeMomentId(1L),
            stateSnapshot = gameState(passedTime = PassedTime(2L.seconds))
        )
        val viewState = mapMainViewState(
            state = gameState(
                plot = plot,
                passedTime = PassedTime(5L.seconds),
                timeMoments = listOf(timeMoment)
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .all {
                prop(MainViewState.Content::plot)
                    .all {
                        prop(PlotViewState::text).isEqualTo(plot.text)
                        prop(PlotViewState::choices)
                            .extracting(ChoiceModel::id, ChoiceModel::text)
                            .containsExactly(choice.id to choice.text)
                    }
                prop(MainViewState.Content::resources)
                    .all {
                        prop(ResourcesViewState::passedTime)
                            .prop(ResourceModel::text)
                            .isEqualTo("5.00s")
                    }
                prop(MainViewState.Content::timeTravel)
                    .prop(TimeTravelViewState::moments)
                    .index(0)
                    .all {
                        prop(TimeMomentModel::id).isEqualTo(timeMoment.id)
                        prop(TimeMomentModel::time).isEqualTo(timeMoment.stateSnapshot.passedTime)
                    }
            }

    }

    @Test
    fun `should show only resources that you have`() {
        val viewState = mapMainViewState(
            state = gameState(
                resources = listOf(
                    resource(id = ResourceId.TimeCrystal, value = 0f),
                    resource(id = ResourceId.Money, value = 100f),
                ),
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::resources)
            .prop(ResourcesViewState::resources)
            .extracting(ResourceModel::id, ResourceModel::text)
            .containsExactly(ResourceId.Money to "100.0")
    }

    @Test
    fun `should show resources storage`() {
        val resourceWithoutStorage = resource(id = ResourceId.Time, value = 5f)
        val resourceWithStorage = resource(id = ResourceId.Money, value = 6f)

        val viewState = mapMainViewState(
            state = gameState(
                resources = listOf(
                    resourceWithoutStorage,
                    resourceWithStorage,
                ),
                storedResources = listOf(
                    storedResource(
                        id = resourceWithStorage.id,
                        current = ResourceValue(50f),
                        max = ResourceValue(100f)
                    )
                )
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::resources)
            .prop(ResourcesViewState::resources)
            .extracting(ResourceModel::id, ResourceModel::stored)
            .containsExactly(
                resourceWithoutStorage.id to null,
                resourceWithStorage.id to "50.0 / 100.0"
            )
    }

    @Test
    fun `should show available actions`() {
        val missingTag = tag(id = TagId(1L), name = "missing")
        val presentTag = tag(id = TagId(2L), name = "present")
        val resourceChangeAvailableAction = action(
            id = ActionId(1L),
            title = "available resource",
            resourceChanges = resourceChanges(
                resourceChange(
                    id = ResourceId.Money,
                    change = -50f
                )
            )
        )
        val resourceChangeNotAvailableAction = action(
            id = ActionId(2L),
            title = "too expensive",
            resourceChanges = resourceChanges(
                resourceChange(
                    id = ResourceId.Money,
                    change = -100f
                )
            )
        )
        val tagAvailableAction = action(
            id = ActionId(3L),
            title = "available tag",
            requiredTags = listOf(presentTag.id),
        )
        val tagNotAvailableAction = action(
            id = ActionId(4L),
            title = "don't have required tag",
            requiredTags = listOf(missingTag.id),
        )
        val viewState = mapMainViewState(
            state = gameState(
                resources = listOf(
                    resource(id = ResourceId.Money, value = 50f),
                ),
                presentTags = listOf(presentTag.id),
                actions = listOf(
                    resourceChangeAvailableAction,
                    resourceChangeNotAvailableAction,
                    tagAvailableAction,
                    tagNotAvailableAction,
                ),
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::actions)
            .extracting(ActionModel::id, ActionModel::title)
            .containsExactly(
                resourceChangeAvailableAction.id to resourceChangeAvailableAction.title,
                tagAvailableAction.id to tagAvailableAction.title,
            )
    }

    @Test
    fun `should set moment parents correctly`() {
        val viewState = mapMainViewState(
            state = gameState(
                timeMoments = listOf(
                    timeMoment(
                        id = TimeMomentId(0L),
                        timelineParentId = null,
                        parents = emptyList()
                    ),
                    timeMoment(
                        id = TimeMomentId(1L),
                        timelineParentId = null,
                        parents = listOf(TimeMomentId(0L))
                    ),
                    timeMoment(
                        id = TimeMomentId(2L),
                        timelineParentId = null,
                        parents = listOf(TimeMomentId(1L))
                    ),
                    timeMoment(
                        id = TimeMomentId(3L),
                        timelineParentId = TimeMomentId(1L),
                        parents = listOf(TimeMomentId(1L))
                    ),
                    timeMoment(
                        id = TimeMomentId(4L),
                        timelineParentId = TimeMomentId(1L),
                        parents = listOf(TimeMomentId(3L))
                    ),
                )
            )
        )

        assertThat(viewState)
            .isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::timeTravel)
            .prop(TimeTravelViewState::moments)
            .extracting(TimeMomentModel::id, TimeMomentModel::momentParents)
            .containsExactly(
                TimeMomentId(0L) to emptyList<TimeMomentId>(),
                TimeMomentId(1L) to listOf(TimeMomentId(0L)),
                TimeMomentId(2L) to listOf(TimeMomentId(1L)),
                TimeMomentId(3L) to listOf(TimeMomentId(1L)),
                TimeMomentId(4L) to listOf(TimeMomentId(3L)),
            )
    }

    @Test
    fun `should form location view state`() {
        val selectedLocation = location(
            id = LocationId(1L),
            title = "lol",
            description = "kek",
        )
        val locations =
            listOf(selectedLocation) + listOf(location(id = LocationId(2L), title = "cheburek"))

        val viewState = mapMainViewState(
            state = gameState(
                allLocations = locations,
                currentLocationId = selectedLocation.id,
            )
        )

        assertThat(viewState)
            .propLocationState()
            .all {
                prop(LocationViewState::description).isEqualTo(selectedLocation.description)
                prop(LocationViewState::selector).all {
                    prop(SelectorViewState::selectedItem).isEqualTo(selectedLocation)
                    prop(SelectorViewState::items).isEqualTo(locations)
                }
            }
    }

    @Test
    fun `should show expanded location selector`() {
        val viewState = mapMainViewState(
            state = gameState(
                selectorExpandedStates = selectorExpandedStates(
                    selectorExpandedState(key = SelectorKey.Location, isExpanded = true),
                )
            )
        )

        assertThat(viewState).assertLocationExpanded(expected = true)
    }

    @Test
    fun `should show collapsed location selector`() {
        val viewState = mapMainViewState(
            state = gameState(
                selectorExpandedStates = selectorExpandedStates(
                    selectorExpandedState(key = SelectorKey.Location, isExpanded = false),
                )
            )
        )

        assertThat(viewState).assertLocationExpanded(expected = false)
    }

    private fun Assert<SelectorModel?>.isEqualTo(location: Location) = isNotNull()
        .prop(SelectorModel::id)
        .prop(SelectorId::raw)
        .isEqualTo(location.id.raw)

    private fun Assert<List<SelectorModel>>.isEqualTo(locations: List<Location>) =
        extracting(SelectorModel::id, SelectorModel::title)
            .containsAll(*locations.map { SelectorId(it.id.raw) to it.title }.toTypedArray())

    private fun Assert<MainViewState>.assertLocationExpanded(expected: Boolean) =
        propLocationState()
            .prop(LocationViewState::selector)
            .assertExpanded(expected)

    private fun Assert<SelectorViewState>.assertExpanded(expected: Boolean) =
        prop(SelectorViewState::isExpanded)
            .isEqualTo(expected)

    private fun Assert<MainViewState>.propLocationState() =
        isInstanceOf(MainViewState.Content::class)
            .prop(MainViewState.Content::location)

}
