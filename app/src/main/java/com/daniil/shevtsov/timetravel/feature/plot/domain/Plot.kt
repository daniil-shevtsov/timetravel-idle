package com.daniil.shevtsov.timetravel.feature.plot.domain

data class Plot(
    val id: PlotId,
    val text: String,
    val choices: List<Choice>,
)

@JvmInline
value class PlotId(val value: Long)

fun plot(
    id: PlotId = PlotId(0L),
    text: String = "",
    choices: List<Choice> = emptyList(),
) = Plot(
    id = id,
    text = text,
    choices = choices,
)
