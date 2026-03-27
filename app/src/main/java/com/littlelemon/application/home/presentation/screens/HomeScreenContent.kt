package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.core.presentation.components.OptionSelect
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.home.presentation.HomeViewModel

@Composable
fun HomeScreenContent(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    HomeScreenRoot(modifier)
}


@Composable
fun HomeScreenRoot(modifier: Modifier = Modifier) {
    val homeOptions = listOf("Food Delivery", "Reserve a Table")
    // TODO: Add navigation switch using navgraphs
    var currentSelection by remember { mutableStateOf(homeOptions[0]) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.size2XL))
        OptionSelect(
            homeOptions,
            selectedOption = currentSelection,
            onSelectionChange = { currentSelection = it },
        )
        Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
        FoodDeliveryScreen()
    }
}


@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
@Composable
private fun HomeScreenPreview() {
    LittleLemonTheme {
        HomeScreenRoot()
    }
}