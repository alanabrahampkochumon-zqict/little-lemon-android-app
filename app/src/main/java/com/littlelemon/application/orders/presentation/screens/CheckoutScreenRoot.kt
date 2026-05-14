package com.littlelemon.application.orders.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.cart.presentation.screen.components.CartItemCard
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.menu.domain.models.Dish
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel


@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: OrderScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CheckoutScreenRoot(state.cartItems, onNavigateBack)
}

@Composable
fun CheckoutScreenRoot(
    cartItems: List<CartDetailItem>,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LittleLemonTheme.colors.primary)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clickable(onClick = onNavigateBack)
                    .minimumInteractiveComponentSize()
            ) {
                Image(
                    painterResource(R.drawable.ic_x),
                    contentDescription = stringResource(R.string.back_to_cart)
                )
            }
            Text(
                stringResource(R.string.review_order),
                style = LittleLemonTheme.typography.displaySmall
            )
        }
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        HorizontalDivider(color = LittleLemonTheme.colors.outlineSecondary)

        LazyColumn(
            contentPadding = PaddingValues(
                vertical = LittleLemonTheme.dimens.size2XL,
                horizontal = LittleLemonTheme.dimens.sizeXL
            ),
            modifier = Modifier.background(LittleLemonTheme.colors.primary),
            verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeXL)
        ) {
            this.items(cartItems) { cartItem ->
                CartItemCard(
                    cartItem,
                    onIncreaseQuantity = { /** TODO */ },
                    onDecreaseQuantity = { /** TODO */ },
                    onRemoveItem = { /** TODO */ },
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(
                        top = LittleLemonTheme.dimens.sizeXL,
                        bottom = LittleLemonTheme.dimens.size4XL,
                        start = LittleLemonTheme.dimens.sizeXL,
                        end = LittleLemonTheme.dimens.sizeXL
                    )
                ) {
                    Header(stringResource(R.string.delivering_to))
                    // Address
                }
            }
        }
    }
    // Order cards
    // Delivering to
    // Order Summary
}


@Preview
@Composable
private fun CheckoutScreenRootPreview() {
    LittleLemonTheme {
        CheckoutScreenRoot(
            listOf(
                CartDetailItem(
                    dish = Dish(
                        "1234", "Dish 1", "Description", 15.59,
                        imageURL = "",
                        stock = 15,
                        nutritionInfo = null,
                        discountedPrice = 12.99,
                        category = emptyList(),
                        dateAdded = LocalDateTime(2026, 12, 12, 30, 30),
                        popularityIndex = 55,
                    )
                )
            ), {})
    }
}

@Preview(widthDp = 1200, heightDp = 1200)
@Composable
private fun CheckoutScreenRootPreviewTab() {
    LittleLemonTheme {
        CheckoutScreenRoot(
            listOf(
                CartDetailItem(
                    dish = Dish(
                        "1234", "Dish 1", "Description", 15.59,
                        imageURL = "",
                        stock = 15,
                        nutritionInfo = null,
                        discountedPrice = 12.99,
                        category = emptyList(),
                        dateAdded = LocalDateTime(2026, 12, 12, 30, 30),
                        popularityIndex = 55,
                    )
                )
            ), {})
    }
}