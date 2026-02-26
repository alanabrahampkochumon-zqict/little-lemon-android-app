package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun AddressEntryModal(modifier: Modifier = Modifier) {

    Column() {
        // MAP or Location Permission

        // Content
        ModalForm()
    }

}

@Composable
private fun ModalForm(modifier: Modifier = Modifier) {

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