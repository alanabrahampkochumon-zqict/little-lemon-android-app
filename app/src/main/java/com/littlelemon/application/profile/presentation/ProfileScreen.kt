package com.littlelemon.application.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.shared.address.presentation.AddressCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val name = state.userName
    val email = state.email

    val addressState by viewModel.addresses.collectAsStateWithLifecycle()

    ///////
    LazyColumn(
        modifier
            .padding(top = LittleLemonTheme.dimens.sizeLG)
            .fillMaxHeight()
            .background(
                LittleLemonTheme.colors.primary,
                LittleLemonTheme.shapes.xl.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ),
        contentPadding = PaddingValues(
            vertical = LittleLemonTheme.dimens.size2XL,
            horizontal = LittleLemonTheme.dimens.sizeLG
        ), verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeXL)
    ) {
        // Basic information
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = LittleLemonTheme.dimens.sizeMD)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .applyShadow(LittleLemonTheme.shapes.xl, LittleLemonTheme.shadows.dropXS)
                        .background(LittleLemonTheme.colors.primary, LittleLemonTheme.shapes.xl)
                        .border(
                            LittleLemonTheme.dimens.size2XS,
                            LittleLemonTheme.colors.outlineSecondary,
                            LittleLemonTheme.shapes.xl
                        )
                ) {
                    Image(
                        painterResource(R.drawable.profile),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp)
                    )
                }
                Spacer(Modifier.width(LittleLemonTheme.dimens.sizeLG))
                Column {
                    Text(name, style = LittleLemonTheme.typography.displayMedium)
                    Text(
                        email,
                        style = LittleLemonTheme.typography.bodySmall,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(LittleLemonTheme.dimens.sizeXL))
            Header(
                stringResource(R.string.addresses),
                typeStyle = HeaderTypeStyle.Secondary
            )
        }
        items(addressState.address) { address ->
            AddressCard(
                address,
                // TODO: Add popup
                { viewModel.onAction(ProfileActions.RemoveAddress(address)) },
                { /** TODO */ },
                { /** TODO */ })
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    LittleLemonTheme {
        ProfileScreen()
    }
}