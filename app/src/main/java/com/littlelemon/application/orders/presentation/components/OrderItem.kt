package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.Stepper
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.orders.domain.models.MenuItem
import kotlinx.datetime.LocalDateTime


@Composable
fun OrderItem(menuItem: MenuItem, modifier: Modifier = Modifier) {
    val orderTotal = menuItem.quantity * menuItem.dish.price
    val discountedTotal = menuItem.quantity * (menuItem.dish.discountedPrice ?: 0.0)
    val discountPercent = if (menuItem.dish.discountedPrice == null) {
        null
    } else {
        (menuItem.dish.price - menuItem.dish.discountedPrice) / menuItem.dish.price * 100
    }
    Column(modifier = modifier) {
        Row() {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray)
            ) // TODO: Replace with image
            Column() {
                Text(text = menuItem.dish.title)
                Text(text = stringResource(R.string.price_format_ea, menuItem.dish.price))
            }
            Column() {
                Row {
                    Text(stringResource(R.string.currency_symbol))
                        Text(
                            stringResource(
                                R.string.price_format,
                                if (menuItem.dish.discountedPrice != null) discountedTotal else orderTotal
                            )
                        )
                }
                discountPercent?.let {
                    Column() {

                Text(stringResource(R.string.currency_symbol) + stringResource(R.string.price_format, orderTotal))
                        Tag(stringResource(R.string.discount_format, discountPercent))
                    }
                }
            }
        }
        Row {
            Button(
                stringResource(R.string.act_remove),
                {},
                variant = ButtonVariant.GHOST,
                modifier = Modifier.weight(1f)
            )
            Stepper(menuItem.quantity, onIncrease = {}, onDecrease = {})
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun OrderItemPreview() {
    LittleLemonTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val dish = Dish(
                title = "Dish 1",
                description = "Very long description",
                price = 14.99,
                imageURL = "",
                stock = 25,
                nutritionInfo = NutritionInfo(150, 1, 2, 3),
                discountedPrice = 8.99,
                category = listOf(),
                dateAdded = LocalDateTime(1999, 12, 30, 11, 11, 11),
                popularityIndex = 11
            )
            OrderItem(MenuItem(dish, 2))
            OrderItem(MenuItem(dish.copy(discountedPrice = null), 4))
        }
    }
}