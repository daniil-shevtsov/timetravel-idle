package com.daniil.shevtsov.timetravel.core.ui.widgets.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.timetravel.core.domain.SelectorId
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

@Preview
@Composable
fun SelectorPreview() {
    Selector(
        state = selectorPreviewData(),
        title = "Lol:",
        onSelected = {},
        onExpandChange = {},
    )
}

@Composable
fun Selector(
    state: SelectorViewState,
    title: String,
    onSelected: (id: SelectorId) -> Unit,
    onExpandChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingM)
    ) {
        Text(
            modifier = Modifier,
            text = title,
            style = AppTheme.typography.bodyTitle,
            color = AppTheme.colors.textLight
        )
        Box(modifier = Modifier.weight(1f).width(IntrinsicSize.Max)) {
            Text(
                text = state.selectedItem?.title ?: "NOT SELECTED",
                style = AppTheme.typography.body,
                color = AppTheme.colors.textDark,
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundText)
                    .clickable(onClick = { onExpandChange() })
                    .padding(AppTheme.dimensions.paddingXS)
            )
            DropdownMenu(
                expanded = state.isExpanded,
                modifier = Modifier.wrapContentHeight(),
                onDismissRequest = { onExpandChange() }) {
                state.items.forEach { item ->
                    DropdownMenuItem(
                        modifier = modifier.background(AppTheme.colors.backgroundText),
                        onClick = {
                            onSelected(item.id)
                            onExpandChange()
                        }
                    ) {
                        Text(
                            text = item.title,
                            style = AppTheme.typography.body,
                            color = AppTheme.colors.textDark
                        )
                    }
                }
            }
        }
    }
}

fun selectorPreviewData() = SelectorViewState(
    items = listOf(
        selectorModel(title = "lol"),
        selectorModel(title = "kek"),
        selectorModel(title = "cheburek"),
    ),
    selectedItem = selectorModel(title = "kek"),
    isExpanded = true,
)
