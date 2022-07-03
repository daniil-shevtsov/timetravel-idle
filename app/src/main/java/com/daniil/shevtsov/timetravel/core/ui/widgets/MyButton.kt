package com.daniil.shevtsov.timetravel.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .background(AppTheme.colors.background)
            .clickable { onClick() }
            .padding(AppTheme.dimensions.paddingS)) {
        Text(
            text = text,
            style = AppTheme.typography.bodyTitle,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textLight,
            modifier = Modifier.wrapContentSize()
        )
    }

}
