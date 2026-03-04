package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.Checkbox
import com.littlelemon.application.core.presentation.components.LabelInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.utils.toComposeShadow

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
    onSaveAddress: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    val bottomSheetShape = MaterialTheme.shapes.medium.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )

    val screenDensity = LocalDensity.current.density

    // Content padding to ensure that the top navigation does not hit the system bar or camera cutout
    val bottomSafeContentPadding = WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
    val topBarBottomPadding = MaterialTheme.dimens.sizeXL
    val navBarHeight = 48.dp
    val mapHeightDP = 400.dp // TODO: Use different height on mobile landscape
    val topBarMinHeight = navBarHeight + bottomSafeContentPadding + topBarBottomPadding
    val mapHeightInPx = (mapHeightDP.value) * screenDensity
    val minTopBarHeightInPx = (topBarMinHeight.value * screenDensity)

    var mapHeight by remember { mutableFloatStateOf(mapHeightInPx) }

    val nestedScrollConnection = remember(mapHeight) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y.toInt()
                val newHeight = mapHeight + delta
                mapHeight = newHeight.coerceIn(minTopBarHeightInPx, mapHeightInPx)
                val consumed = mapHeight - newHeight
                return Offset(0f, consumed)
            }
        }
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection), bottomBar = {
            FlowRow(
                modifier = Modifier
                    .dropShadow(
                        shape = bottomSheetShape,
                        MaterialTheme.shadows.dropLG.firstShadow.toComposeShadow(screenDensity)
                    )
                    .dropShadow(
                        shape = bottomSheetShape,
                        MaterialTheme.shadows.dropLG.secondShadow?.toComposeShadow(screenDensity)
                            ?: Shadow(0.dp)
                    )
                    .background(MaterialTheme.colors.primary, shape = bottomSheetShape)
                    .navigationBarsPadding()
                    .padding(MaterialTheme.dimens.sizeXL),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)
            ) {
                Button(
                    stringResource(R.string.act_cancel),
                    onClose,
                    variant = ButtonVariant.GHOST_HIGHLIGHT,
                    modifier = Modifier
                        .widthIn(min = 320.dp)
                        .weight(1f)
                )
                Button(
                    stringResource(R.string.act_save_address),
                    onClick = onSaveAddress,
                    modifier = Modifier
                        .widthIn(min = 320.dp)
                        .weight(1f)
                )
            }
        }, topBar = {
            Box(
                modifier = Modifier
                    .height((mapHeight / screenDensity).dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.TopCenter
            ) {
                FloatingActionBar(
                    modifier = Modifier.padding(
                        top = bottomSafeContentPadding,
                        bottom = topBarBottomPadding
                    ), onAction = onClose
                )
                Text(
                    "TODO: Google Map",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        })
    { innerPadding ->
        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            ModalForm(
                state,
                modifier = Modifier.padding(innerPadding),
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
}

@Composable
fun FloatingActionBar(modifier: Modifier = Modifier, onAction: () -> Unit = {}) {
    val density = LocalDensity.current.density
    val floatingBarShape = MaterialTheme.shapes.extraLarge
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeLG),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.dimens.sizeXL, end = MaterialTheme.dimens.size4XL)
    ) {
        IconButton(
            onClick = onAction, colors = IconButtonColors(
                containerColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.contentSecondary,
                disabledContainerColor = MaterialTheme.colors.disabled,
                disabledContentColor = MaterialTheme.colors.contentDisabled
            ),
            modifier = Modifier
                .size(48.dp)
                .testTag(AddressTestTags.HEADER_CLOSE_BUTTON)
        ) {
            Image(painterResource(R.drawable.ic_x), contentDescription = "Close")
        }

        Box(
            Modifier
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.firstShadow.toComposeShadow(density)
                )
                .dropShadow(
                    floatingBarShape,
                    MaterialTheme.shadows.dropLG.secondShadow?.toComposeShadow(density)
                        ?: Shadow(0.dp)
                )
                .background(MaterialTheme.colors.primary, shape = floatingBarShape)
                .padding(MaterialTheme.dimens.sizeLG)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.heading_add_your_address))
        }
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
                top = MaterialTheme.dimens.sizeXL,
                bottom = MaterialTheme.dimens.sizeXL
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
            keyboardActions = KeyboardActions(onGo = { onSaveAddress() })
        )

        Checkbox(
            state.isDefaultAddress,
            onCheckedChange = { onSaveAsDefaultChange(it) },
            label = stringResource(R.string.label_address_save_as_default),
            modifier = Modifier
                .testTag(stringResource(R.string.test_tag_address_save_as_default))
                .fillMaxWidth()
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