package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun AddressPicker(
    address: String,
    modifier: Modifier = Modifier,
    onAddressChange: () -> Unit,
    deliverable: Boolean = true,
    elevated: Boolean = false
) {

    val icon = if (deliverable) R.drawable.ic_map_point else R.drawable.ic_close_circle
    val iconColor =
        if (deliverable) LittleLemonTheme.colors.contentAccentSecondary else LittleLemonTheme.colors.contentError
    val heading = if (deliverable) R.string.label_delivering else R.string.label_not_delivering
    val shape = LittleLemonTheme.shapes.sm
    val surfaceColor =
        if (elevated) LittleLemonTheme.colors.primary else LittleLemonTheme.colors.secondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (elevated) Modifier.applyShadow(
                    shape,
                    LittleLemonTheme.shadows.dropXS
                ) else Modifier
            )
            .background(surfaceColor, shape)
            .clickable(
                indication = ripple(bounded = true), onClick = onAddressChange,
                interactionSource = remember { MutableInteractionSource() },
            )
            .padding(
                top = LittleLemonTheme.dimens.sizeMD,
                bottom = LittleLemonTheme.dimens.sizeMD,
                start = LittleLemonTheme.dimens.sizeMD,
                end = LittleLemonTheme.dimens.sizeLG
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            null,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(LittleLemonTheme.dimens.sizeSM))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(heading),
                style = LittleLemonTheme.typography.bodyXSmall,
                color = LittleLemonTheme.colors.contentPlaceholder
            )
            Spacer(Modifier.height(LittleLemonTheme.dimens.sizeXS))
            Text(
                text = address,
                style = LittleLemonTheme.typography.labelMedium,
                color = LittleLemonTheme.colors.contentTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(LittleLemonTheme.dimens.sizeSM))
        Image(
            painterResource(R.drawable.ic_chevron_down),
            null,
            colorFilter = ColorFilter.tint(LittleLemonTheme.colors.contentPlaceholder),
            modifier = Modifier.size(16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AddressPickerPreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AddressPicker(
                "Work (Lincoln Park) ",
                deliverable = true,
                elevated = false,
                onAddressChange = {})
            AddressPicker(
                "Work (Lincoln Park) ",
                deliverable = false,
                elevated = false,
                onAddressChange = {})
            AddressPicker(
                "Work (Very Long Address that will overflow) ",
                deliverable = false,
                elevated = false, onAddressChange = {}
            )

            AddressPicker(
                "Work (Lincoln Park) ",
                deliverable = true,
                elevated = true,
                onAddressChange = {})
            AddressPicker(
                "Work (Lincoln Park) ",
                deliverable = false,
                elevated = true,
                onAddressChange = {})
        }
    }
}