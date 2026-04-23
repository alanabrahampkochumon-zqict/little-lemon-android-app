package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import kotlin.math.abs

@Composable
fun PriceRow(label: String, price: Double, modifier: Modifier = Modifier) {
    var formattedPrice = ""
    if (price < 0.0)
        formattedPrice += "-"
    formattedPrice += stringResource(R.string.currency_symbol)
    formattedPrice += stringResource(R.string.price_format, abs(price))

    var priceColor = LittleLemonTheme.colors.contentSecondary
    if (abs(price) < 0.001) {
        formattedPrice = "FREE"
        priceColor = LittleLemonTheme.colors.contentSuccess
    }

    Row(modifier) {
        Text(
            label,
            style = LittleLemonTheme.typography.bodyMedium,
            color = LittleLemonTheme.colors.contentSecondary,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(LittleLemonTheme.dimens.sizeLG))
        Text(
            formattedPrice,
            style = LittleLemonTheme.typography.labelMedium.copy(textAlign = TextAlign.End),
            color = priceColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceRowPreview() {
    LittleLemonTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PriceRow("Subtotal", 88.53)
        PriceRow("Discount", -38.82)
        PriceRow("Shipping charges", 0.0)

        }
    }
}