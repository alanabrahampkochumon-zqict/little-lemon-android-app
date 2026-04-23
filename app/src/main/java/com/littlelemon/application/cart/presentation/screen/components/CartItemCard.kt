package com.littlelemon.application.cart.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.littlelemon.application.R
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.presentation.components.BasicStepper
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime

// TODO: Add tests
// TODO: Change stepper
// TODO: Check connect button
@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = LittleLemonTheme.shapes.sm
    val imageOffset = LittleLemonTheme.dimens.sizeLG

    val fontOffset = -LittleLemonTheme.dimens.sizeSM

    val orderTotal = cartItem.quantity * cartItem.dish.price
    val discountedTotal = cartItem.quantity * (cartItem.dish.discountedPrice ?: 0.0)
    Column(
        modifier = modifier
            .applyShadow(cardShape, LittleLemonTheme.shadows.dropSM)
            .background(LittleLemonTheme.colors.primary, shape = cardShape)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = LittleLemonTheme.dimens.sizeLG,
                    end = LittleLemonTheme.dimens.sizeLG
                )
                .zIndex(1f)
        ) {
            AsyncImage(
                model = cartItem.dish.imageURL,
                modifier = Modifier
                    .size(80.dp)
                    .offset(y = imageOffset)
                    .background(
                        LittleLemonTheme.colors.disabled,
                        shape = LittleLemonTheme.shapes.xs
                    ),
                placeholder = painterResource(R.drawable.ic_gallery), // TODO: Change
                contentDescription = cartItem.dish.title
            )
            Spacer(Modifier.width(LittleLemonTheme.dimens.sizeMD))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = imageOffset),
                verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.size2XS)
            ) {
                Text(
                    text = cartItem.dish.title,
                    style = LittleLemonTheme.typography.labelLarge,
                    color = LittleLemonTheme.colors.contentPrimary
                )
                Text(
                    text = stringResource(R.string.price_format_ea, cartItem.dish.price),
                    style = LittleLemonTheme.typography.bodySmall,
                    color = LittleLemonTheme.colors.contentTertiary
                )
            }
            Spacer(Modifier.width(LittleLemonTheme.dimens.sizeMD))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(top = imageOffset)
                    .offset(y = fontOffset)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.currency_symbol),
                        style = LittleLemonTheme.typography.displaySmall.copy(textAlign = TextAlign.End),
                        color = LittleLemonTheme.colors.contentOnAction,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        stringResource(
                            R.string.price_format,
                            if (cartItem.dish.discountedPrice != null) discountedTotal else orderTotal
                        ),
                        style = LittleLemonTheme.typography.displayMedium.copy(textAlign = TextAlign.End),
                        color = LittleLemonTheme.colors.contentOnAction,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                cartItem.dish.discountedPrice?.let {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.offset(y = fontOffset)
                    ) {
                        Text(
                            stringResource(R.string.currency_symbol) + stringResource(
                                R.string.price_format,
                                orderTotal
                            ),
                            style = LittleLemonTheme.typography.bodyXSmall.copy(
                                textAlign = TextAlign.End,
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = LittleLemonTheme.colors.contentTertiary
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .background(
                    LittleLemonTheme.colors.primaryLight,
                    cardShape.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
                )
                .padding(start = LittleLemonTheme.dimens.size2XL)
        ) {
            Button(
                stringResource(R.string.act_remove),
                onRemoveItem,
                variant = ButtonVariant.GHOST,
                modifier = Modifier.weight(1f)
            )
            BasicStepper(
                cartItem.quantity,
                onIncrease = onIncreaseQuantity,
                onDecrease = onDecreaseQuantity
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CartItemCardPreview() {
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
                imageURL = "https://images.pexels.com/photos/1108117/pexels-photo-1108117.jpeg",
                stock = 25,
                nutritionInfo = NutritionInfo(150, 1, 2, 3),
                discountedPrice = 8.99,
                category = listOf(),
                dateAdded = LocalDateTime(1999, 12, 30, 11, 11, 11),
                popularityIndex = 11
            )
            CartItemCard(CartItem(dish, 2), {}, {}, {})
            CartItemCard(CartItem(dish.copy(discountedPrice = null), 4), {}, {}, {})
        }
    }
}