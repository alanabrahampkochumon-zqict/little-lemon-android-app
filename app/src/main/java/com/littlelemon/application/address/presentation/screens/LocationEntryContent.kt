package com.littlelemon.application.address.presentation.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressActions
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.components.MapHeader
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.Checkbox
import com.littlelemon.application.core.presentation.components.LabelInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.core.presentation.utils.toDP

@Composable
fun LocationEntryContent(
    viewModel: AddressViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    isFloating: Boolean = false
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    LocationEntryContentRoot(
        state,
        modifier = modifier,
        onLabelChange = { viewModel.onAction(AddressActions.ChangeLabel(it)) },
        onBuildingNameChange = { viewModel.onAction(AddressActions.ChangeBuildingName(it)) },
        onStreetAddressChange = { viewModel.onAction(AddressActions.ChangeStreetAddress(it)) },
        onCityChange = { viewModel.onAction(AddressActions.ChangeCity(it)) },
        onStateChange = { viewModel.onAction(AddressActions.ChangeState(it)) },
        onPinCodeChange = { viewModel.onAction(AddressActions.ChangePinCode(it)) },
        onSaveAsDefaultChange = { viewModel.onAction(AddressActions.ChangeToDefaultAddress(it)) },
        onSaveAddress = {
            viewModel.onAction(AddressActions.SaveAddress)
            onClose()
        },
        onClose = onClose,
        isFloating = isFloating
    )

}

@Composable
fun LocationEntryContentRoot(
    state: AddressState,
    modifier: Modifier = Modifier,
    isFloating: Boolean = false,
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
        bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)
    )
    val screenDensity = LocalDensity.current.density
    val screenWidth = LocalWindowInfo.current.containerDpSize.width
    val mobileLandscape = screenWidth > 600.dp && !isFloating

    // Content padding to ensure that the top navigation does not hit the system bar or camera cutout
    val navBarHeight = 48.dp
    val mapHeightDP = 400.dp
    val safeContentPadding = WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
    val topBarBottomPadding = MaterialTheme.dimens.sizeXL
    val topBarMinHeight = navBarHeight + safeContentPadding + topBarBottomPadding

    val mapHeightInPx = (mapHeightDP.value) * screenDensity
    val minTopBarHeightInPx = (topBarMinHeight.value * screenDensity)

    var mapHeight by remember { mutableFloatStateOf(mapHeightInPx) }

    val animatedHeight = animateFloatAsState(
        mapHeight, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
    )

    //TODO: Disable maps when scrolled up to min size
    val nestedScrollConnection = remember(mapHeight) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y.toInt()
                val newHeight = mapHeight + delta
                mapHeight = newHeight.coerceIn(minTopBarHeightInPx, mapHeightInPx)
                val consumed = mapHeight - newHeight
                return Offset(0f, consumed)
            }

            // TODO: Fix animation using animateable
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                mapHeight =
                    if (mapHeight > mapHeightInPx / 2) // If the current fling height 1/2 of the way then snap it to full height
                        mapHeightInPx
                    else minTopBarHeightInPx
                return super.onPostFling(consumed, available)
            }
        }
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag(AddressTestTags.NESTED_SCROLL_VIEW)
            .then(if (!isFloating && !mobileLandscape) Modifier.nestedScroll(nestedScrollConnection) else Modifier),
        bottomBar = {
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
                if (mobileLandscape)
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
                        .weight(1f),
                    variant = ButtonVariant.HIGH_CONTRAST
                )
            }
        },
        topBar = {
            // Render as topbar if not mobile landscape
            if (!mobileLandscape) MapHeader(
                modifier = Modifier
                    .height(animatedHeight.value.toDP(screenDensity))
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .testTag(AddressTestTags.NESTED_SCROLL_HEADER),
                onClose = onClose,
                floatingBarTopPadding = safeContentPadding,
                floatingBarBottomPadding = topBarBottomPadding,
                onFetchCurrentLocation = { TODO("Implementation") }
            )
        }) { innerPadding ->
        if (!mobileLandscape) Column(
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
        else Row {
            MapHeader(
                myLatitude = state.latitude,
                myLongitude = state.longitude,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.DarkGray)
                    .testTag(AddressTestTags.NESTED_SCROLL_HEADER),
                onClose = onClose,
                floatingBarTopPadding = safeContentPadding,
                floatingBarBottomPadding = topBarBottomPadding,
                onFetchCurrentLocation = { TODO("Implementation") }
            )

            ModalForm(
                state,
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
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

// TODO: Add consume insetpadding to other composables.


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
    val context = LocalContext.current

    FlowRow(
        modifier = modifier
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
            onValueChange = onLabelChange,
            modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        LabelInputField(
            label = stringResource(R.string.label_address_building_name),
            placeholder = stringResource(R.string.placeholder_address_building_name),
            value = state.buildingName,
            errorMessage = state.buildingNameError?.asString(context),
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
            errorMessage = state.streetAddressError?.asString(context),
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
            errorMessage = state.cityError?.asString(context),
            onValueChange = onCityChange,
            modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_state),
            placeholder = stringResource(R.string.placeholder_address_state),
            value = state.state,
            errorMessage = state.stateError?.asString(context),
            onValueChange = onStateChange,
            modifier = Modifier
                .widthIn(min = 280.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        LabelInputField(
            label = stringResource(R.string.label_address_pincode),
            placeholder = stringResource(R.string.placeholder_address_pincode),
            value = state.pinCode,
            errorMessage = state.pinCodeError?.asString(context),
            onValueChange = onPinCodeChange,
            modifier = Modifier
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


///////////////////////////////////////////////////////////
//                     PREVIEWS                          //
///////////////////////////////////////////////////////////

@Preview(showSystemUi = true)
@Composable
private fun AddressEntryModalPreview() {

    LittleLemonTheme {
        LocationEntryContentRoot(AddressState())
    }
}

@Preview(widthDp = 800, heightDp = 360)
@Composable
private fun AddressEntryModalLandscapePreview() {

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

@Preview(heightDp = 1225)
@Composable
private fun AddressEntryModalErrorPreview() {

    LittleLemonTheme {
        LocationEntryContentRoot(
            AddressState(
                label = "Home",
                buildingName = "1234 Building name",
                buildingNameError = UiText.DynamicString("Building name error"),
                streetAddress = "Street Address",
                streetAddressError = UiText.DynamicString("Street address error"),
                city = "City",
                cityError = UiText.DynamicString("City error"),
                state = "State",
                stateError = UiText.DynamicString("State error"),
                pinCode = "123456",
                pinCodeError = UiText.DynamicString("Pincode error")
            )
        )
    }
}