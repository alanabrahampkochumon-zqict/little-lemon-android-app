package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun FoodDeliveryScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Header(label = stringResource(R.string.heading_order_for_delivery), typeStyle = HeaderTypeStyle.Secondary)
    }
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
@Composable
private fun FoodDeliveryScreenPreview() {
    LittleLemonTheme {
        FoodDeliveryScreen()
    }
}