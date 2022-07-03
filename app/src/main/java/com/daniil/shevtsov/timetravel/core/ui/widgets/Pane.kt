package com.daniil.shevtsov.timetravel.core.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme

@Composable
fun <T> Pane(
    items: List<T>,
    itemContent: @Composable (item: T, modifier: Modifier) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)
    ) {
        (items.indices step 2)
            .map { index -> items[index] to items.getOrNull(index + 1) }
            .forEach { (startAction, endAction) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {
                    itemContent(startAction, Modifier.let { modifier ->
                        if (endAction == null) {
                            modifier.fillMaxWidth()
                        } else {
                            modifier
                        }
                            .weight(1f)
                            .fillMaxHeight()
                    })
                    if (endAction != null) {
                        itemContent(
                            endAction, Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
    }
}
