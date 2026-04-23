package com.littlelemon.application.cart.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.cart.presentation.screen.components.CartItemCard
import com.littlelemon.application.core.presentation.components.PriceRow
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

@Composable
fun CartScreenContent(modifier: Modifier = Modifier) {
    Row(modifier) {
        PricingSection(modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        ItemSection(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun PricingSection(modifier: Modifier = Modifier) {
    // TODO: REPLACE
    val cartQuantity = 5
    val price = 88.58
    val taxes = 15.95
    val discount = -14.58
    val shipping = 0.0
    val total = 104.56
    val formattedTotal =
        stringResource(R.string.currency_symbol) + stringResource(R.string.price_format, total)
    ////////
    Column(
        modifier = Modifier
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
            PriceRow(stringResource(R.string.subtotal), price)
            PriceRow(label = stringResource(R.string.taxes), taxes)
            PriceRow(stringResource(R.string.discounts), discount)
            PriceRow(stringResource(R.string.shipping), shipping)
        }
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        HorizontalDivider(color = LittleLemonTheme.colors.outlineSecondary)
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeMD))
        Row {
            Text(
                stringResource(R.string.amount_payable),
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
    }
}

@Composable
fun ItemSection(modifier: Modifier = Modifier) {
    val cartItems = List(5) {
        Dish(
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
    }.map { CartItem(it, Random.nextInt(3, 5)) }

    LazyColumn(
        modifier.padding(
        ),
        contentPadding = PaddingValues(
            horizontal = LittleLemonTheme.dimens.sizeLG,
            vertical = LittleLemonTheme.dimens.size2XL
        ),
        verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.size2XL)
    ) {
        items(cartItems) { cartItem ->
            CartItemCard(cartItem = cartItem, { /** TODO */ }, { /** TODO */ }, { /** TODO */ })
        }
    }

}


@Preview(showBackground = true, backgroundColor = 0xccc)
@Composable
private fun CartScreenContentPreview() {

    val dishes = List(5) {
        Dish(
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
    }.map { CartItem(it, Random.nextInt(3, 5)) }

    LittleLemonTheme {
        CartScreenContent()
    }
}