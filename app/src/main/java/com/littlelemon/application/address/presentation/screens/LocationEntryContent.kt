package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors

@Composable
fun AddressEntryModal(modifier: Modifier = Modifier) {

    Column() {
        // MAP or Location Permission
        Box(modifier = Modifier.height(400.dp).fillMaxWidth().background(Color.DarkGray), contentAlignment = Alignment.Center) {
            Text("TODO: Google Map", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        // Content
        ModalForm()
    }

}

@Composable
private fun ModalForm(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colors.primary)) {
        // Label
//        Text(stringResource(R.string.label_address_label))
    }
}

@Composable
fun TextFieldWithLabel(modifier: Modifier = Modifier) {
    // TODO: Reuse from "Personalization screen"
}

@Preview
@Composable
private fun AddressEntryModalPreview() {

    LittleLemonTheme {
        AddressEntryModal()
    }
}