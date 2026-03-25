package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.address.presentation.mappers.toFullAddress
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xSmall
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import kotlin.math.min


@Composable
fun AddressList(
    addressList: List<LocalAddress>,
    selected: LocalAddress,
    onSelectionChange: (address: LocalAddress) -> Unit,
    onViewAllAddress: () -> Unit,
    modifier: Modifier = Modifier
) {

    val unnamedAddress = stringResource(R.string.unnamed_address_label)
    val locationStringRes = R.string.location_string
    val unknownRes = stringResource(R.string.not_found_cap)
    val shape = MaterialTheme.shapes.medium
    val density = LocalDensity.current.density
    val firstShadow = MaterialTheme.shadows.dropLG.firstShadow.toComposeShadow(density)
    val secondShadow =
        MaterialTheme.shadows.dropLG.secondShadow?.toComposeShadow(density) ?: Shadow(0.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(shape, firstShadow)
            .dropShadow(shape, secondShadow)
            .background(MaterialTheme.colors.primary, shape)
            .padding(
                MaterialTheme.dimens.sizeMD
            ), verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM)
    ) {
        AddressListItem(
            addressLabel = selected.label ?: unnamedAddress,
            address = selected.address?.toFullAddress() ?: stringResource(
                locationStringRes,
                selected.location?.latitude ?: unknownRes,
                selected.location?.longitude ?: unknownRes
            ),
            selected = true,
            onSelectionChange = {
                onSelectionChange(selected)
            })
        addressList.filter { address -> address != selected }.take(min(addressList.size - 1, 2))
            .forEach { address ->
                AddressListItem(
                    addressLabel = address.label ?: unnamedAddress,
                    address = address.address?.toFullAddress() ?: stringResource(
                        locationStringRes,
                        address.location?.latitude ?: unknownRes,
                        address.location?.longitude ?: unknownRes
                    ),
                    selected = false,
                    onSelectionChange = {
                        onSelectionChange(address)
                    })
            }
        Button(
            stringResource(R.string.view_all),
            onClick = onViewAllAddress,
            variant = ButtonVariant.GHOST_HIGHLIGHT
        )
    }
}


@Composable
fun AddressListItem(
    addressLabel: String,
    address: String,
    selected: Boolean,
    onSelectionChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    val shape = MaterialTheme.shapes.xSmall
    val shadow =
        MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(LocalDensity.current.density)

    Column(
        modifier
            .fillMaxWidth()
            .dropShadow(shape, shadow)
            .then(
                if (selected) Modifier.border(
                    1.dp, MaterialTheme.colors.contentAccentSecondary, shape
                ) else Modifier
            )
            .background(MaterialTheme.colors.primary, shape)
            .padding(
                horizontal = MaterialTheme.dimens.sizeLG, vertical = MaterialTheme.dimens.sizeMD
            )
            .selectable(
                selected = selected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelectionChange
            )
    ) {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = addressLabel,
                style = MaterialTheme.typeStyle.bodySmall,
                color = MaterialTheme.colors.contentTertiary,
                modifier = Modifier.weight(1f)
            )
            // TODO: Add animation
            if (selected) {
                Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
                Image(
                    painterResource(R.drawable.ic_checkcircle_filled),
                    null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.contentAccentSecondary),
                    modifier = Modifier
                        .size(20.dp)
                        .testTag(CoreTestTags.ADDRESS_ITEM_CHECK_ICON)
                )
            }
        }
        Spacer(Modifier.height(MaterialTheme.dimens.sizeSM))
        Text(
            address,
            style = MaterialTheme.typeStyle.labelMedium,
            color = MaterialTheme.colors.contentSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}


////////////////////////////////////////////////////////
//                   PREVIEW
////////////////////////////////////////////////////////

@Preview(showBackground = true)
@Composable
private fun AddressListPreview() {
    val address = listOf(
        LocalAddress(
            id = "id1",
            label = "Label",
            address = PhysicalAddress(
                address = "Address 1",
                streetAddress = "Street Address 1",
                city = "City 1",
                state = "State 1",
                pinCode = "123456"
            ),
            location = null,
            isDefault = true
        ),
        LocalAddress(
            id = "id2",
            label = "Label",
            address = PhysicalAddress(
                address = "Address 2",
                streetAddress = "Street Address 2",
                city = "City 2",
                state = "State 2",
                pinCode = "123456"
            ),
            location = null,
            isDefault = true
        ),
        LocalAddress(
            id = "id3",
            label = "Label",
            address = PhysicalAddress(
                address = "Address 3",
                streetAddress = "Street Address 3",
                city = "City 3",
                state = "State 3",
                pinCode = "123456"
            ),
            location = null,
            isDefault = true
        ),
        LocalAddress(
            id = "id4",
            label = "Label",
            address = PhysicalAddress(
                address = "Address 4",
                streetAddress = "Street Address 4",
                city = "City 4",
                state = "State 4",
                pinCode = "123456"
            ),
            location = null,
            isDefault = true
        )
    )
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AddressList(address, address[0], {}, {})
        AddressList(address.subList(0, 1), address[0], {}, {})
        AddressList(address.subList(0, 2), address[0], {}, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressListItemPreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AddressListItem(
                "Work", "16281 Washington Avenue, Lincoln Park, Chicago - 60614", true, {})
            AddressListItem(
                "Work", "16281 Washington Avenue, Lincoln Park, Chicago - 60614", false, {})
        }
    }
}