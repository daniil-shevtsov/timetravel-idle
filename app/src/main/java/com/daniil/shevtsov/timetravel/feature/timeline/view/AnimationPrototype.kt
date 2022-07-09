package com.daniil.shevtsov.timetravel.feature.timeline.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimationPrototypePreview() {
    AnimationPrototype()
}

@Composable
fun AnimationPrototype(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .background(Color.Green)
        .size(300.dp)) {

    }
}
