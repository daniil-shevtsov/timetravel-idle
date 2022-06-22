package com.daniil.shevtsov.timetravel.feature.plot.domain

data class Choice(
    val id: ChoiceId,
    val text: String,
    val destinationPlotId: PlotId,
)

@JvmInline
value class ChoiceId(val value: Long)

fun choice(
    id: ChoiceId = ChoiceId(0L),
    text: String = "",
    destinationPlotId: PlotId = PlotId(0L)
) = Choice(
    id = id,
    text = text,
    destinationPlotId = destinationPlotId,
)

