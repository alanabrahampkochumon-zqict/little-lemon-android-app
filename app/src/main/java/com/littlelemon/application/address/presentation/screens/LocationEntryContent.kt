package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressActions
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.presentation.components.Checkbox
import com.littlelemon.application.core.presentation.components.LabelInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens

@Composable
fun LocationEntryContent(viewModel: AddressViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LocationEntryContentRoot(
        state,
        onLabelChange = { viewModel.onAction(AddressActions.ChangeLabel(it)) },
        onBuildingNameChange = { viewModel.onAction(AddressActions.ChangeBuildingName(it)) },
        onStreetAddressChange = { viewModel.onAction(AddressActions.ChangeStreetAddress(it)) },
        onCityChange = { viewModel.onAction(AddressActions.ChangeCity(it)) },
        onStateChange = { viewModel.onAction(AddressActions.ChangeState(it)) },
        onPinCodeChange = { viewModel.onAction(AddressActions.ChangePinCode(it)) },
        onSaveAsDefaultChange = { viewModel.onAction(AddressActions.ChangeToDefaultAddress(it)) },
        onSaveAddress = { viewModel.onAction(AddressActions.SaveAddress) })

}

@Composable
fun LocationEntryContentRoot(
    state: AddressState, modifier: Modifier = Modifier,
    onLabelChange: (String) -> Unit = {},
    onBuildingNameChange: (String) -> Unit = {},
    onStreetAddressChange: (String) -> Unit = {},
    onCityChange: (String) -> Unit = {},
    onStateChange: (String) -> Unit = {},
    onPinCodeChange: (String) -> Unit = {},
    onSaveAsDefaultChange: (Boolean) -> Unit = {},
    onSaveAddress: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "TODO: Google Map",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        ModalForm(
            state,
            onLabelChange = onLabelChange,
            onBuildingNameChange = onBuildingNameChange,
            onStreetAddressChange = onStreetAddressChange,
            onCityChange = onCityChange,
            onStateChange = onStateChange,
            onPinCodeChange = onPinCodeChange,
            onSaveAsDefaultChange = onSaveAsDefaultChange,
            onSaveAddress = onSaveAddress,
        )
    }
}

@Composable
fun ModalForm(
    state: AddressState,
    modifier: Modifier = Modifier,
    onLabelChange: (String) -> Unit = {},
    onBuildingNameChange: (String) -> Unit = {},
    onStreetAddressChange: (String) -> Unit = {},
    onCityChange: (String) -> Unit = {},
    onStateChange: (String) -> Unit = {},
    onPinCodeChange: (String) -> Unit = {},
    onSaveAsDefaultChange: (Boolean) -> Unit = {},
    onSaveAddress: () -> Unit = {},
) {

    FlowRow(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(
                start = MaterialTheme.dimens.sizeXL,
                end = MaterialTheme.dimens.sizeXL,
                top = MaterialTheme.dimens.size4XL,
                bottom = MaterialTheme.dimens.size2XL
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2XL),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2XL)
    ) {

        LabelInputField(
            label = stringResource(R.string.label_address_label),
            placeholder = stringResource(R.string.placeholder_address_label),
            value = state.label,
            onValueChange = onLabelChange, modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_building_name),
            placeholder = stringResource(R.string.placeholder_address_building_name),
            value = state.buildingName,
            onValueChange = onBuildingNameChange,
            modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_street_address),
            placeholder = stringResource(R.string.placeholder_address_street_address),
            value = state.streetAddress,
            onValueChange = onStreetAddressChange,
            modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_city),
            placeholder = stringResource(R.string.placeholder_address_city),
            value = state.city,
            onValueChange = onCityChange, modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_state),
            placeholder = stringResource(R.string.placeholder_address_state),
            value = state.state,
            onValueChange = onStateChange, modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_pincode),
            placeholder = stringResource(R.string.placeholder_address_pincode),
            value = state.pinCode,
            onValueChange = onPinCodeChange, modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {onSaveAddress()})
        )

        Checkbox(
            state.isDefaultAddress,
            onCheckedChange = onSaveAsDefaultChange,
            label = stringResource(R.string.label_address_save_as_default),
            modifier = Modifier.testTag(stringResource(R.string.test_tag_address_save_as_default))
        )


    }
}

@Preview
@Composable
private fun AddressEntryModalPreview() {

    LittleLemonTheme {
        LocationEntryContentRoot(AddressState())
    }
}

@Preview
@Composable
private fun AddressEntryModalFilledPreview() {

    LittleLemonTheme {
        LocationEntryContentRoot(
            AddressState(
                label = "Home",
                buildingName = "1234 Building name",
                streetAddress = "Street Address",
                city = "City",
                state = "State",
                pinCode = "123456"
            )
        )
    }
}