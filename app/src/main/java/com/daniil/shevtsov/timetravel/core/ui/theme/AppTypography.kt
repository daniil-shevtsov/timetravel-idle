package com.daniil.shevtsov.timetravel.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val default = FontFamily.Default
private val openSans = FontFamily.Monospace

data class AppTypography(
    val header: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
    ),
    val title: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    val bodyTitle: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    val body: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    val icon: TextStyle = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
