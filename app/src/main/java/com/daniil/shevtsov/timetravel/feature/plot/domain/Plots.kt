package com.daniil.shevtsov.timetravel.feature.plot.domain

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
            )
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
)
