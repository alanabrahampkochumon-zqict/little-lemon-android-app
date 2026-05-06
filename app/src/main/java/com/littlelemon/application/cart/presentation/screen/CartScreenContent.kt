package com.littlelemon.application.cart.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.cart.presentation.CartAction
import com.littlelemon.application.cart.presentation.CartState
import com.littlelemon.application.cart.presentation.CartViewModel
import com.littlelemon.application.cart.presentation.screen.components.CartItemCard
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.PriceRow
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun CartScreenContent(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>()
) {
    val state by cartViewModel.state.collectAsStateWithLifecycle()
    val (priceBeforeDiscount, priceAfterDiscount) = state.cartDetailItems.fold(0.0 to 0.0) { initial, cartDetailItem ->
        val runningTotal = initial.first + cartDetailItem.dish.price
        val runningDiscountedTotal =
            initial.second + (cartDetailItem.dish.discountedPrice ?: cartDetailItem.dish.price)
        runningTotal to runningDiscountedTotal
    }
    Log.d("Cart", state.toString())
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.size2XL)
    ) {
        item {
            PricingSection(
                subtotal = priceBeforeDiscount,
                discountedPrice = priceAfterDiscount,
                cartQuantity = state.cartDetailItems.size,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        }
        cartItemsSection(
            state, onIncreaseQuantity = { cartViewModel.onAction(CartAction.IncreaseQuantity(it)) },
            onDecreaseQuantity = { cartViewModel.onAction(CartAction.DecreaseQuantity(it)) },
            onRemoveItem = { cartViewModel.onAction(CartAction.RemoveItem(it)) }
        )
    }
}


@Composable
fun PricingSection(
    subtotal: Double,
    discountedPrice: Double,
    cartQuantity: Int,
    modifier: Modifier = Modifier
) {
    val formattedTotal =
        stringResource(R.string.currency_symbol) + stringResource(
            R.string.price_format,
            discountedPrice
        )
    Column(
        modifier = modifier
            .background(LittleLemonTheme.colors.secondary, LittleLemonTheme.shapes.md)
            .padding(
                horizontal = LittleLemonTheme.dimens.sizeXL,
                vertical = LittleLemonTheme.dimens.size2XL
            )
    ) {
        Text(
            stringResource(R.string.heading_your_cart, cartQuantity),
            style = LittleLemonTheme.typography.displayMedium
        )
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeLG))
        Column(verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeXS)) {
            PriceRow(stringResource(R.string.subtotal), subtotal)
            PriceRow(stringResource(R.string.discounts), subtotal - discountedPrice)
        }
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        HorizontalDivider(color = LittleLemonTheme.colors.outlineSecondary)
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        Row {
            Text(
                stringResource(R.string.subtotal_ex_taxes),
                style = LittleLemonTheme.typography.bodyMedium,
                color = LittleLemonTheme.colors.contentSecondary,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(LittleLemonTheme.dimens.sizeLG))
            Text(
                formattedTotal,
                style = LittleLemonTheme.typography.displaySmall.copy(textAlign = TextAlign.End),
                color = LittleLemonTheme.colors.contentPrimary
            )
        }
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeXL))
        Button(
            stringResource(R.string.proceed_to_checkout),
            variant = ButtonVariant.HIGH_CONTRAST,
            onClick = { /** TODO */ })
    }
}

fun LazyListScope.cartItemsSection(
    state: CartState,
    onIncreaseQuantity: (CartDetailItem) -> Unit,
    onDecreaseQuantity: (CartDetailItem) -> Unit,
    onRemoveItem: (CartDetailItem) -> Unit
) {
    val cartDetailItems = List(5) {
        Dish(
            id = "",
            title = "Dish $it",
            description = "Long description for the dish that is generated at index $it",
            price = 14.99,
            imageURL = "https://images.pexels.com/photos/1108117/pexels-photo-1108117.jpeg",
            stock = 25,
            nutritionInfo = NutritionInfo(150, 1, 2, 3),
            discountedPrice = 8.99,
            category = listOf(),
            dateAdded = LocalDateTime(1999, 12, 30, 11, 11, 11),
            popularityIndex = 11
        )
    }.map { CartDetailItem(it, Random.nextInt(3, 5)) }

    items(state.cartDetailItems) { cartItem ->
        Box(modifier = Modifier.padding(horizontal = LittleLemonTheme.dimens.sizeXL)) {
            CartItemCard(
                cartDetailItem = cartItem,
                { onIncreaseQuantity(cartItem) },
                { onDecreaseQuantity(cartItem) },
                { onRemoveItem(cartItem) })
        }
    }

}


@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
private fun CartScreenContentPreview() {

    val dishes = List(5) {
        Dish(
            id = "",
            title = "Dish $it",
            description = "Long description for the dish that is generated at index $it",
            price = 14.99,
            imageURL = "https://images.pexels.com/photos/1108117/pexels-photo-1108117.jpeg",
            stock = 25,
            nutritionInfo = NutritionInfo(150, 1, 2, 3),
            discountedPrice = 8.99,
            category = listOf(),
            dateAdded = LocalDateTime(1999, 12, 30, 11, 11, 11),
            popularityIndex = 11
        )
    }.map { CartDetailItem(it, Random.nextInt(3, 5)) }

    LittleLemonTheme {
        CartScreenContent()
    }
}