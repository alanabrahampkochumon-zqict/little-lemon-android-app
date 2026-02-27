package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.presentation.components.LabelInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens

@Composable
fun LocationEntryContent(viewModel: AddressViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LocationEntryContentRoot(state)

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
    onSaveAsDefaultChange: (Boolean) -> Unit = {}
) {
    Column(modifier = modifier) {
        // MAP or Location Permission
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
        // Content
        ModalForm(
            state,
            onLabelChange = onLabelChange,
            onBuildingNameChange = onBuildingNameChange,
            onStreetAddressChange = onStreetAddressChange,
            onCityChange = onCityChange,
            onStateChange = onStateChange,
            onPinCodeChange = onPinCodeChange,
            onSaveAsDefaultChange = onSaveAsDefaultChange
        )
    }
}

@Composable
private fun ModalForm(
    state: AddressState,
    modifier: Modifier = Modifier,
    onLabelChange: (String) -> Unit = {},
    onBuildingNameChange: (String) -> Unit = {},
    onStreetAddressChange: (String) -> Unit = {},
    onCityChange: (String) -> Unit = {},
    onStateChange: (String) -> Unit = {},
    onPinCodeChange: (String) -> Unit = {},
    onSaveAsDefaultChange: (Boolean) -> Unit = {},
) {

    val screenWidth = LocalWindowInfo.current.containerDpSize.width
    // TODO: 2 Col on 560.dp and above
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .verticalScroll(rememberScrollState())
            .padding(
                start = MaterialTheme.dimens.sizeXL,
                end = MaterialTheme.dimens.sizeXL,
                top = MaterialTheme.dimens.size4XL,
                bottom = MaterialTheme.dimens.sizeXL
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2XL)
    ) {
        LabelInputField(
            label = stringResource(R.string.label_address_label),
            placeholder = stringResource(R.string.placeholder_address_label),
            value = state.label,
            onValueChange = onLabelChange
        )
        LabelInputField(
            label = stringResource(R.string.label_address_building_name),
            placeholder = stringResource(R.string.placeholder_address_building_name),
            value = state.buildingName,
            onValueChange = onBuildingNameChange
        )
        LabelInputField(
            label = stringResource(R.string.label_address_street_address),
            placeholder = stringResource(R.string.placeholder_address_street_address),
            value = state.streetAddress,
            onValueChange = onStreetAddressChange
        )
        LabelInputField(
            label = stringResource(R.string.label_address_city),
            placeholder = stringResource(R.string.placeholder_address_city),
            value = state.city,
            onValueChange = onCityChange
        )
        LabelInputField(
            label = stringResource(R.string.label_address_state),
            placeholder = stringResource(R.string.placeholder_address_state),
            value = state.state,
            onValueChange = onStateChange
        )
        LabelInputField(
            label = stringResource(R.string.label_address_pincode),
            placeholder = stringResource(R.string.placeholder_address_pincode),
            value = state.pinCode,
            onValueChange = onPinCodeChange
        )

        // TODO: Replace with checkbox
        // TODO: Replace test tag on the checkbox
        Text(
            stringResource(R.string.label_address_save_as_default),
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