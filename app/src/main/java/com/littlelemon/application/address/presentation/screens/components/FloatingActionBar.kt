package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow


@Composable
fun FloatingActionBar(title: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    val floatingBarShape = LittleLemonTheme.shapes.xl
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeLG),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = LittleLemonTheme.dimens.sizeXL, end = LittleLemonTheme.dimens.size4XL)
    ) {
        IconButton(
            onClick = onAction, colors = IconButtonColors(
                containerColor = LittleLemonTheme.colors.primary,
                contentColor = LittleLemonTheme.colors.contentSecondary,
                disabledContainerColor = LittleLemonTheme.colors.disabled,
                disabledContentColor = LittleLemonTheme.colors.contentDisabled
            ), modifier = Modifier
                .size(48.dp)
                .testTag(AddressTestTags.HEADER_CLOSE_BUTTON)
                .applyShadow(
                    floatingBarShape,
                    LittleLemonTheme.shadows.dropSM
                )
        ) {
            Image(painterResource(R.drawable.ic_x), contentDescription = "Close")
        }

        Box(
            Modifier
                .applyShadow(
                    floatingBarShape,
                    LittleLemonTheme.shadows.dropSM
                )
                .background(LittleLemonTheme.colors.primary, shape = floatingBarShape)
                .padding(LittleLemonTheme.dimens.sizeLG)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                title,
                style = LittleLemonTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
                color = LittleLemonTheme.colors.contentPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FloatingActionBarPreview() {

    LittleLemonTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            FloatingActionBar("Action Bar", {})
        }
    }

}