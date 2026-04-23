package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun AddressCard(
    addressLabel: String,
    fullAddress: String,
    default: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    Column(
        modifier
            .applyShadow( LittleLemonTheme.shapes.sm, LittleLemonTheme.shadows.dropXS)
            .background(LittleLemonTheme.colors.primary, LittleLemonTheme.shapes.sm)
            .padding(
                vertical = LittleLemonTheme.dimens.sizeXL,
                horizontal = LittleLemonTheme.dimens.sizeLG
            )
    ) {
        Text(addressLabel, style = LittleLemonTheme.typography.displaySmall, color = LittleLemonTheme.colors.contentSecondary)
        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeXS))
        Text(fullAddress, style = LittleLemonTheme.typography.bodySmall, color = LittleLemonTheme.colors.contentTertiary)
    }
}


@Preview(showBackground = true)
@Composable
private fun AddressCardPreview() {

    LittleLemonTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(12.dp)) {
            AddressCard(
                "Workplace", "16281 Washington Avenue,\nLincoln Park,\nChicago - 60614", true, true
            )
            AddressCard(
                "Workplace", "16281 Washington Avenue,\nLincoln Park,\nChicago - 60614", true, false
            )
            AddressCard(
                "Workplace", "16281 Washington Avenue,\nLincoln Park,\nChicago - 60614", false, false
            )
        }
    }

}