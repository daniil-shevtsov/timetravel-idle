package com.daniil.shevtsov.timetravel.feature.plot.domain

import com.daniil.shevtsov.timetravel.feature.timeline.domain.TagId

data class Plot(
    val id: PlotId,
    val text: String,
    val tagsToAdd: List<TagId>,
    val choices: List<Choice>,
)

@JvmInline
value class PlotId(val value: Long)

fun plot(
    id: PlotId = PlotId(0L),
    text: String = "",
    tagsToAdd: List<TagId> = emptyList(),
    choices: List<Choice> = emptyList(),
) = Plot(
    id = id,
    text = text,
    choices = choices,
    tagsToAdd = tagsToAdd,
)
