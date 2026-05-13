package com.littlelemon.application.shared.address.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow


@Composable
fun AddressCard(
    address: LocalAddress,
    onDeleteAddress: () -> Unit,
    onEditAddress: () -> Unit,
    onMakeDefault: () -> Unit,
    modifier: Modifier = Modifier
) {

    val shape = LittleLemonTheme.shapes.lg

    var addressString = ""
    if (address.address != null) {
        addressString += if (address.address.address.isNotBlank()) address.address.address + ",\n" else ""
        addressString += if (address.address.streetAddress.isNotBlank()) address.address.streetAddress + ",\n" else ""
        addressString += if (address.address.city.isNotBlank()) address.address.city + ",\n" else ""
        addressString += address.address.state.ifBlank { "" }
        addressString += if (address.address.pinCode.isNotBlank()) " - ${address.address.pinCode}" else ""

    }

    if (addressString.isBlank() && address.location != null) {
        "(${address.location.latitude}, ${address.location.longitude})"
    }

    if (addressString.isBlank()) {
        stringResource(R.string.unknown_location)
    }

    val label =
        if (address.label?.isNullOrBlank() == false) address.label else stringResource(R.string.unnamed_address_label)

    Column(
        modifier = Modifier
            .applyShadow(shape, LittleLemonTheme.shadows.dropSM)
            .background(LittleLemonTheme.colors.primary, shape).clip(shape)
            .padding(
                start = LittleLemonTheme.dimens.sizeXL,
                top = LittleLemonTheme.dimens.sizeLG
            )
    ) {
        Text(
            label,
            style = LittleLemonTheme.typography.displaySmall
        )
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeSM))
        Text(addressString, style = LittleLemonTheme.typography.bodySmall)
//        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeLG))
        Row {
            if (address.isDefault)
                Tag(stringResource(R.string.default_tag), variant = TagVariant.SuccessFilled)

            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                Box(
                    modifier = Modifier
                        .clickable(onClick = onEditAddress)
                        .minimumInteractiveComponentSize()
                ) {
                    Image(
                        painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.delete_address)
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(onClick = onDeleteAddress)
                        .minimumInteractiveComponentSize()
                ) {
                    Image(
                        painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.delete_address)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun AddressCardPreview() {

    LittleLemonTheme {
        AddressCard(
            address = LocalAddress(
                id = "1234", label = "Physical Address", address = PhysicalAddress(
                    address = "Some street",
                    streetAddress = "some street address",
                    city = "Some city",
                    state = "Some state",
                    pinCode = "12349324"
                )
            ), {}, {}, {}
        )
    }
}