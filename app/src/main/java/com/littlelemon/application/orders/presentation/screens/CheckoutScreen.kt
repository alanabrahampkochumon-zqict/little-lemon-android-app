package com.littlelemon.application.orders.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun CheckoutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LittleLemonTheme.colors.primary)
    ) {
        Row {
            Image(
                painterResource(R.drawable.ic_x),
                contentDescription = stringResource(R.string.back_to_cart)
            )
//            Text(stringResource(R.string.review_order))
        }
        // Order cards
        // Delivering to
        // Order Summary
    }
}


@Preview
@Composable
private fun CheckoutScreenPreview() {
    LittleLemonTheme {
        CheckoutScreen()
    }
}