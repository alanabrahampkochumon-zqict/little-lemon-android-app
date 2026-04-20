package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun DotSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(LittleLemonTheme.dimens.sizeXS)
            .background(
                LittleLemonTheme.colors.contentDisabled,
                shape = LittleLemonTheme.shapes.xl
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun DotSeparatorPreview() {
    LittleLemonTheme {
        Box(Modifier.padding(12.dp)) {
            DotSeparator()
        }
    }
}