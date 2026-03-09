package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.xLarge

@Composable
fun DotSeparator(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .size(MaterialTheme.dimens.sizeSM)
            .background(MaterialTheme.colors.contentTertiary, shape = MaterialTheme.shapes.xLarge)
            .then(modifier)
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