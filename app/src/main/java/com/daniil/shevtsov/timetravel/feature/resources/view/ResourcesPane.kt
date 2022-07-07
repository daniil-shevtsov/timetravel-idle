package com.daniil.shevtsov.timetravel.feature.resources.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.main.view.WithTitle
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ValidTransferDirection

@Preview
@Composable
fun ResourcesPanePreview() {
    ResourcesPane(
        state = resourcesPanePreviewData(),
        onTakeResource = {},
        onStoreResource = {},
    )
}

@Composable
fun ResourcesPane(
    state: ResourcesViewState,
    onTakeResource: (key: ResourceId) -> Unit,
    onStoreResource: (key: ResourceId) -> Unit,
    modifier: Modifier = Modifier,
) {
    WithTitle(title = "Resources", modifier = modifier) {

        Column(modifier = modifier.width(IntrinsicSize.Max)) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS)) {
                Text(
                    text = state.passedTime.title,
                    color = AppTheme.colors.textLight,
                    style = AppTheme.typography.bodyTitle,
                )
                Text(
                    text = state.passedTime.text,
                    textAlign = TextAlign.End,
                    color = AppTheme.colors.textLight,
                    style = AppTheme.typography.body,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            state.resources.forEach { resource ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingS),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = resource.title,
                        color =AppTheme.colors.textLight,
                        style = AppTheme.typography.bodyTitle,
                    )
                    Text(
                        text = resource.text,
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.textLight,
                        style = AppTheme.typography.body,
                        modifier = Modifier,
                    )
                    if (resource.stored != null) {
                        IconButton(
                            onClick = { onTakeResource(resource.id) }, modifier = modifier
                                .size(16.dp),
                            enabled = resource.enabledDirections == ValidTransferDirection.Take || resource.enabledDirections == ValidTransferDirection.Both
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = modifier.fillMaxSize(),
                                tint = AppTheme.colors.iconLight,
                            )
                        }
                        IconButton(
                            onClick = { onStoreResource(resource.id) }, modifier = modifier
                                .size(16.dp),
                            enabled = resource.enabledDirections == ValidTransferDirection.Store || resource.enabledDirections == ValidTransferDirection.Both
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Forward",
                                modifier = modifier.fillMaxSize(),
                                tint = AppTheme.colors.iconLight,
                            )
                        }
                        Text(
                            text = resource.stored,
                            textAlign = TextAlign.End,
                            color = AppTheme.colors.textLight,
                            style = AppTheme.typography.body,
                            modifier = Modifier,
                        )
                    }

                }
            }
        }
    }
}

fun resourcesPanePreviewData() = ResourcesViewState(
    passedTime = ResourceModel(
        id = ResourceId.Time,
        title = "Passed Time",
        text = "5.00 s",
    ),
    resources = listOf(
        ResourceModel(
            id = ResourceId.Money,
            title = "Money",
            text = "100 $",
            stored = "75 / 500 $",
        )
    ),
)
