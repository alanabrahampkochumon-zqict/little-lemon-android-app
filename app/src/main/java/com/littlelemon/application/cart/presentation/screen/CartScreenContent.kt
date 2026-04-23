package com.littlelemon.application.cart.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun CartScreenContent(modifier: Modifier = Modifier) {
    Row(modifier) {
        PricingSection()
        ItemSection()
    }
}

@Composable
fun PricingSection(modifier: Modifier = Modifier) {
    // TODO: REPLACE
    val cartQuantity = 5
    ////////
    Column(modifier = Modifier.padding(horizontal = LittleLemonTheme.dimens.sizeXL, vertical = LittleLemonTheme.dimens.size2XL)) {
        Text(stringResource(R.string.heading_your_cart, cartQuantity))
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeLG))
    }
}

@Composable
fun ItemSection(modifier: Modifier = Modifier) {

}


@Preview
@Composable
private fun CartScreenContentPreview() {
    LittleLemonTheme {
        CartScreenContent()
    }
}