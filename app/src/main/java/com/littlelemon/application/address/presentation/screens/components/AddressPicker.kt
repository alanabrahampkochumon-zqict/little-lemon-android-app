package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow

@Composable
fun AddressPicker(
    address: String,
    modifier: Modifier = Modifier,
    deliverable: Boolean = true,
    elevated: Boolean = false
) {

    val icon = if (deliverable) R.drawable.ic_map_point else R.drawable.ic_close_circle
    val iconColor =
        if (deliverable) MaterialTheme.colors.contentAccentSecondary else MaterialTheme.colors.contentError
    val heading = if (deliverable) R.string.label_delivering else R.string.label_not_delivering
    val shape = MaterialTheme.shapes.medium
    val shadow =
        MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(LocalDensity.current.density)
    val surfaceColor =
        if (elevated) MaterialTheme.colors.primary else MaterialTheme.colors.secondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (elevated) Modifier.dropShadow(shape, shadow) else Modifier)
            .background(surfaceColor, shape)
            .padding(
                top = MaterialTheme.dimens.sizeMD,
                bottom = MaterialTheme.dimens.sizeMD,
                start = MaterialTheme.dimens.sizeMD,
                end = MaterialTheme.dimens.sizeLG
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon),
            null,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(MaterialTheme.dimens.sizeSM))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(heading),
                style = MaterialTheme.typeStyle.bodyXSmall,
                color = MaterialTheme.colors.contentPlaceholder
            )
            Spacer(Modifier.height(MaterialTheme.dimens.sizeXS))
            Text(
                text = address,
                style = MaterialTheme.typeStyle.labelMedium,
                color = MaterialTheme.colors.contentTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(MaterialTheme.dimens.sizeSM))
        Image(
            painterResource(R.drawable.ic_chevron_down),
            null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.contentPlaceholder),
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
            AddressPicker("Work (Lincoln Park) ", deliverable = true, elevated = false)
            AddressPicker("Work (Lincoln Park) ", deliverable = false, elevated = false)
            AddressPicker(
                "Work (Very Long Address that will overflow) ",
                deliverable = false,
                elevated = false
            )

            AddressPicker("Work (Lincoln Park) ", deliverable = true, elevated = true)
            AddressPicker("Work (Lincoln Park) ", deliverable = false, elevated = true)
        }
    }
}