package com.daniil.shevtsov.timetravel.feature.location.view

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
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.location.domain.Location
import com.daniil.shevtsov.timetravel.feature.location.domain.LocationId
import com.daniil.shevtsov.timetravel.feature.location.domain.location

@Preview
@Composable
fun LocationSelectorPreview() {
    LocationSelector(
        locations = listOf(
            location(title = "lol"),
            location(title = "kek"),
            location(title = "cheburek"),
        ),
        selectedLocation = location(title = "kek"),
        isExpanded = true,
        onSelected = {},
        onExpandChange = {},
    )
}

@Composable
fun LocationSelector(
    locations: List<Location>,
    selectedLocation: Location,
    isExpanded: Boolean,
    onSelected: (id: LocationId) -> Unit,
    onExpandChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingM),
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingXS)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingM)
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = "Current location:",
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight
            )
            Box(modifier = modifier.weight(1f)) {
                Text(
                    text = selectedLocation.title,
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.textDark,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.backgroundText)
                        .clickable(onClick = { onExpandChange() })
                        .padding(AppTheme.dimensions.paddingXS)
                )
                DropdownMenu(
                    expanded = isExpanded,
                    modifier = modifier.wrapContentHeight(),
                    onDismissRequest = { onExpandChange() }) {
                    locations.forEach { location ->
                        DropdownMenuItem(
                            modifier = modifier.background(AppTheme.colors.backgroundText),
                            onClick = {
                                onSelected(location.id)
                                onExpandChange()
                            }
                        ) {
                            Text(text = location.toString(), color = AppTheme.colors.textDark)
                        }
                    }
                }
            }
        }
        Text(
            text = selectedLocation.title,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            modifier = modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundText)
                .clickable(onClick = { onExpandChange() })
                .padding(AppTheme.dimensions.paddingXS)
        )
    }
}
