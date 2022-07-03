package com.daniil.shevtsov.timetravel.feature.plot.domain

import com.daniil.shevtsov.timetravel.feature.tags.domain.Change
import com.daniil.shevtsov.timetravel.feature.tags.domain.Tags
import com.daniil.shevtsov.timetravel.feature.tags.domain.tagChange
import com.daniil.shevtsov.timetravel.feature.tags.domain.tagChanges

fun createInitialPlots() = listOf(
    plot(
        id = PlotId(0L),
        text = "Finally, the time machine really works. What are you gonna do?",
        choices = listOf(
            choice(
                id = ChoiceId(0L),
                text = "Sell it to the government",
                destinationPlotId = PlotId(1L),
            ),
            choice(
                id = ChoiceId(1L),
                text = "Use it for personal gain",
                destinationPlotId = PlotId(2L),
            ),
            choice(
                id = ChoiceId(2L),
                text = "Cause nuclear apocalypse",
                destinationPlotId = PlotId(3L),
            ),
            choice(
                id = ChoiceId(3L),
                text = "Cause post-apocalypse",
                destinationPlotId = PlotId(4L),
            ),
        )
    ),
    plot(
        id = PlotId(1L),
        text = "For some time you enjoy everything you couldn't afford before, but then comes a realization, just what you have sacrificed for it",
    ),
    plot(
        id = PlotId(2L),
        text = "You get a quick buck with relatively small changes",
    ),
    plot(
        id = PlotId(3L),
        text = "The world is a nuclear wasteland, there were no survivors.",
        tagChanges = tagChanges(
            tagChange(id = Tags.WorldState.NuclearWasteland.tag.id, change = Change.Add),
            tagChange(id = Tags.WorldState.OrdinaryWorld.tag.id, change = Change.Remove),
            tagChange(id = Tags.Society.Functioning.tag.id, change = Change.Remove),
        )
    ),
    plot(
        id = PlotId(4L),
        text = "Most of the world population went extinct, but there are some survivors.",
        tagChanges = tagChanges(
            tagChange(id = Tags.WorldState.PostApocalypse.tag.id, change = Change.Add),
            tagChange(id = Tags.WorldState.OrdinaryWorld.tag.id, change = Change.Remove),
            tagChange(id = Tags.Society.Functioning.tag.id, change = Change.Remove),
            tagChange(id = Tags.Society.PostApocalyptic.tag.id, change = Change.Add),
        )
    ),
)
