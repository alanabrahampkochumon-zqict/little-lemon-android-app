package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun MiniLoader(
    label: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = LittleLemonTheme.colors.primary

    val contentColor = LittleLemonTheme.colors.contentSecondary

    val shape = LittleLemonTheme.shapes.sm

    Row(
        modifier = modifier
            .applyShadow(shape, LittleLemonTheme.shadows.dropXL)
            .background(backgroundColor, shape)
            .padding(
                vertical = LittleLemonTheme.dimens.sizeMD,
                horizontal = LittleLemonTheme.dimens.sizeLG
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IndefiniteCircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(LittleLemonTheme.dimens.sizeXL)
                .testTag(CoreTestTags.PROGRESS_INDICATOR)
        )
        Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeLG))
        Text(label, style = LittleLemonTheme.typography.labelMedium, color = contentColor)
    }

}

@Preview(showBackground = true)
@Composable
private fun MiniLoaderPreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MiniLoader(
                "Almost there. Tailoring your experience…",
            )
        }
    }
}