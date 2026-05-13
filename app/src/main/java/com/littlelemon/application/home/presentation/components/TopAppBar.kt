package com.littlelemon.application.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.core.presentation.components.AddressPicker
import com.littlelemon.application.core.presentation.components.PrimaryIconButton
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.home.HomeTestTags

// TODO: Add address to address picker
// TODO: Add address picker tests
@Composable
fun TopAppBar(
    defaultAddress: LocalAddress?,
    addressLoading: Boolean,
    addressError: String?,
    onAddressClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape =
        LittleLemonTheme.shapes.lg.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
    val address = if (!addressLoading && defaultAddress != null && addressError == null) {
        var address = ""
        if (defaultAddress.label?.isNotBlank() == true)
            address += defaultAddress.label
        address += if (address.isNotEmpty())
            "(${if (defaultAddress.address?.address?.isNotBlank() == true) defaultAddress.address.address else defaultAddress.address?.streetAddress})"
        else
            "${if (defaultAddress.address?.address?.isNotBlank() == true) defaultAddress.address.address else defaultAddress.address?.streetAddress}"
        address
    } else if (addressLoading)
        stringResource(R.string.address_loading) // TODO: Update to shimmering loader
    else addressError ?: stringResource(R.string.address_loading_error_message)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .applyShadow(
                shape,
                LittleLemonTheme.shadows.dropSM
            )
            .background(LittleLemonTheme.colors.primary, shape)
            .padding(
                start = LittleLemonTheme.dimens.sizeXL,
                end = LittleLemonTheme.dimens.sizeXL,
                bottom = LittleLemonTheme.dimens.sizeXL
            )
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(R.drawable.logo_full), null, modifier = Modifier
                .height(40.dp)
                .testTag(
                    HomeTestTags.LOGO
                )
        )
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeLG))
        Row {
            AddressPicker(
                address,
                modifier = Modifier
                    .weight(1f)
                    .testTag(HomeTestTags.ADDRESS_BAR),
                onAddressChange = onAddressClick
            )
//            Spacer(Modifier.width(LittleLemonTheme.dimens.sizeMD))
//            PrimaryIconButton(
//                R.drawable.ic_search,
//                onClick = onSearchClick,
//                modifier
//                    .size(52.dp)
//                    .testTag(HomeTestTags.SEARCH_BUTTON)
//            )
        }
    }
}


@Preview
@Composable
private fun TopAppBarPreview() {
    LittleLemonTheme {
        TopAppBar(
            LocalAddress(
                id = "1234",
                label = "Home",
                address = PhysicalAddress(
                    address = "1234 Building Name",
                    streetAddress = "Javier's Street",
                    city = "Chicago",
                    state = "Illinois",
                    pinCode = "123485"
                ),
                location = LocalLocation(1.234, 12.343),
                isDefault = true
            ), false, null, {}, )

        TopAppBar(
            LocalAddress(
                id = "1234",
                label = "Home",
                address = PhysicalAddress(
                    address = "1234 Building Name",
                    streetAddress = "Javier's Street",
                    city = "Chicago",
                    state = "Illinois",
                    pinCode = "123485"
                ),
                location = LocalLocation(1.234, 12.343),
                isDefault = true
            ), true, null, {})
    }
}