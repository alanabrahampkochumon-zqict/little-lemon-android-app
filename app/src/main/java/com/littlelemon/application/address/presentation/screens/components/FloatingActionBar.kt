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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow


@Composable
fun FloatingActionBar(title: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val floatingBarShape = MaterialTheme.shapes.extraLarge
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeLG),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.dimens.sizeXL, end = MaterialTheme.dimens.size4XL)
    ) {
        IconButton(
            onClick = onAction, colors = IconButtonColors(
                containerColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.contentSecondary,
                disabledContainerColor = MaterialTheme.colors.disabled,
                disabledContentColor = MaterialTheme.colors.contentDisabled
            ), modifier = Modifier
                .size(48.dp)
                .testTag(AddressTestTags.HEADER_CLOSE_BUTTON)
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.firstShadow.toComposeShadow(density)
                )
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.secondShadow?.toComposeShadow(density)
                        ?: Shadow(0.dp)
                )
        ) {
            Image(painterResource(R.drawable.ic_x), contentDescription = "Close")
        }

        Box(
            Modifier
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.firstShadow.toComposeShadow(density)
                )
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.secondShadow?.toComposeShadow(density)
                        ?: Shadow(0.dp)
                )
                .background(MaterialTheme.colors.primary, shape = floatingBarShape)
                .padding(MaterialTheme.dimens.sizeLG)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                title,
                style = MaterialTheme.typeStyle.labelLarge.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colors.contentPrimary
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