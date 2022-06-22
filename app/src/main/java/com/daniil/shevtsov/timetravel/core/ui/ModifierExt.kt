package com.daniil.shevtsov.timetravel.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.innerBorder(
    startTopColor: Color,
    bottomEndColor: Color,
    size: Dp = 1.dp,
) = background(startTopColor)
    .padding(top = size, start = size)
    .background(bottomEndColor)
    .padding(bottom = size, end = size)
