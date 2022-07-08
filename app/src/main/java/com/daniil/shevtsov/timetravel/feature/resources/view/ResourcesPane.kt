package com.daniil.shevtsov.timetravel.feature.resources.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.DoubleArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.timetravel.core.ui.theme.AppTheme
import com.daniil.shevtsov.timetravel.feature.main.view.WithTitle
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceModel
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourceTransferAmount
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ResourcesViewState
import com.daniil.shevtsov.timetravel.feature.resources.presentation.ValidTransferDirection

@Preview
@Composable
fun ResourcesPanePreview() {
    Column {
        ResourcesPane(
            state = resourcesPanePreviewData().let { state ->
                state.copy(
                    resources = state.resources.map { resource ->
                        resource.copy(
                            stored = null,
                            enabledDirections = ValidTransferDirection.None,
                        )
                    }
                )
            },
            modifier = Modifier,
            onTakeResource = { _, _ -> },
            onStoreResource = { _, _ -> },
        )
        ResourcesPane(
            state = resourcesPanePreviewData(),
            modifier = Modifier,
            onTakeResource = { _, _ -> },
            onStoreResource = { _, _ -> },
        )
    }

}

@Composable
fun ResourcesPane(
    state: ResourcesViewState,
    onTakeResource: (key: ResourceId, ResourceTransferAmount) -> Unit,
    onStoreResource: (key: ResourceId, ResourceTransferAmount) -> Unit,
    modifier: Modifier = Modifier,
) {
    WithTitle(title = "Resources", modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Inventory",
                    color = AppTheme.colors.textLight,
                    style = AppTheme.typography.bodyTitle,
                )
                if (state.resources.any { it.stored != null }) {
                    Text(
                        text = "Storage",
                        color = AppTheme.colors.textLight,
                        style = AppTheme.typography.bodyTitle,
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                state.resources.forEach { resource ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = resource.title,
                                color = AppTheme.colors.textLight,
                                style = AppTheme.typography.bodyTitle,
                                modifier = Modifier
                            )
                            Text(
                                text = resource.text,
                                textAlign = TextAlign.End,
                                color = AppTheme.colors.textLight,
                                style = AppTheme.typography.body,
                                modifier = Modifier,
                                maxLines = 1,
                            )
                        }
                        if (resource.stored != null) {
                            Row(
                                modifier = Modifier.weight(1f, false),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(
                                    onClick = {
                                        onTakeResource(
                                            resource.id,
                                            ResourceTransferAmount.Max
                                        )
                                    },
                                    modifier = modifier
                                        .size(16.dp),
                                    enabled = resource.enabledDirections == ValidTransferDirection.Take || resource.enabledDirections == ValidTransferDirection.Both
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DoubleArrow,
                                        contentDescription = "Get max from storage",
                                        modifier = Modifier.fillMaxSize().scale(scaleX = -1f, scaleY = 1f),
                                        tint = AppTheme.colors.iconLight,
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        onTakeResource(
                                            resource.id,
                                            ResourceTransferAmount.One
                                        )
                                    },
                                    modifier = modifier
                                        .size(16.dp),
                                    enabled = resource.enabledDirections == ValidTransferDirection.Take || resource.enabledDirections == ValidTransferDirection.Both
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Get one from storage",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = AppTheme.colors.iconLight,
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        onStoreResource(
                                            resource.id,
                                            ResourceTransferAmount.One
                                        )
                                    },
                                    modifier = modifier
                                        .size(16.dp),
                                    enabled = resource.enabledDirections == ValidTransferDirection.Store || resource.enabledDirections == ValidTransferDirection.Both
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowForward,
                                        contentDescription = "Put one to storage",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = AppTheme.colors.iconLight,
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        onStoreResource(
                                            resource.id,
                                            ResourceTransferAmount.Max
                                        )
                                    },
                                    modifier = modifier
                                        .size(16.dp),
                                    enabled = resource.enabledDirections == ValidTransferDirection.Store || resource.enabledDirections == ValidTransferDirection.Both
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DoubleArrow,
                                        contentDescription = "Put max to storage",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = AppTheme.colors.iconLight,
                                    )
                                }
                            }
                            Text(
                                text = resource.stored,
                                textAlign = TextAlign.End,
                                color = AppTheme.colors.textLight,
                                style = AppTheme.typography.body,
                                modifier = Modifier.width(100.dp),
                            )
                        }
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
        ),
        ResourceModel(
            id = ResourceId.TimeCrystal,
            title = "Long Name",
            text = "150",
            stored = "200 / 500 $",
        ),
        ResourceModel(
            id = ResourceId.NuclearWaste,
            title = "Very Very Long Name",
            text = "150",
            stored = "200 / 500 $",
        )
    ),
)
