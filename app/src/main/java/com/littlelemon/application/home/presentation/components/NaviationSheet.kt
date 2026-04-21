package com.littlelemon.application.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.AddressPicker
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun NavigationSheet(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(LittleLemonTheme.colors.secondary)
    ) {
        // Navbar
        Column(
            Modifier
                .widthIn(min = 280.dp, max = 360.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Red)
        ) {
            Image(painterResource(R.drawable.logo_full), null)
            AddressPicker("Some address: Replace")
        }
    }
}

@Composable
fun NavigationItem(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) { }
}


@Preview(widthDp = 1200, heightDp = 1400)
@Composable
private fun NavigationSheetPreview() {
    LittleLemonTheme {
        NavigationSheet()
    }
}