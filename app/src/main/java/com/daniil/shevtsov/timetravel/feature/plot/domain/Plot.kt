package com.daniil.shevtsov.timetravel.feature.plot.domain

import com.daniil.shevtsov.timetravel.feature.tags.domain.TagChanges
import com.daniil.shevtsov.timetravel.feature.tags.domain.tagChanges

data class Plot(
    val id: PlotId,
    val text: String,
    val tagChanges: TagChanges,
    val choices: List<Choice>,
)

@JvmInline
value class PlotId(val value: Long)

fun plot(
    id: PlotId = PlotId(0L),
    text: String = "",
    tagChanges: TagChanges = tagChanges(),
    choices: List<Choice> = emptyList(),
) = Plot(
    id = id,
    text = text,
    choices = choices,
    tagChanges = tagChanges,
)
