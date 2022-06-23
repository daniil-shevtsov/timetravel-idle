package com.daniil.shevtsov.timetravel.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val paddingXS: Dp = 4.dp,
    val paddingS: Dp = 8.dp,
    val paddingM: Dp = 12.dp,
    val paddingL: Dp = 16.dp,
    val paddingXL: Dp = 24.dp
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
