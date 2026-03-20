package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.Stepper
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xSmall
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.orders.domain.models.MenuItem
import kotlinx.datetime.LocalDateTime

// TODO: Add tests
// TODO: Change stepper
// TODO: Check connect button
@Composable
fun OrderItem(
    menuItem: MenuItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = MaterialTheme.shapes.small
    val cardShadow = MaterialTheme.shadows.dropSM
    val density = LocalDensity.current.density
    val imageOffset = MaterialTheme.dimens.sizeLG

    val orderTotal = menuItem.quantity * menuItem.dish.price
    val discountedTotal = menuItem.quantity * (menuItem.dish.discountedPrice ?: 0.0)
    val discountPercent = if (menuItem.dish.discountedPrice == null) {
        null
    } else {
        (menuItem.dish.price - menuItem.dish.discountedPrice) / menuItem.dish.price * 100
    }
    Column(
        modifier = modifier
            .dropShadow(cardShape, cardShadow.firstShadow.toComposeShadow(density))
            .background(MaterialTheme.colors.primary, shape = cardShape)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.dimens.sizeLG,
                    end = MaterialTheme.dimens.sizeLG
                )
                .zIndex(1f)
        ) {
            AsyncImage(
                model = menuItem.dish.imageURL,
                modifier = Modifier
                    .size(80.dp)
                    .offset(y = imageOffset)
                    .background(
                        MaterialTheme.colors.primaryDark,
                        shape = MaterialTheme.shapes.xSmall
                    ),
                placeholder = painterResource(R.drawable.illustration_image_loading),
                contentDescription = menuItem.dish.title
            )
            Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = imageOffset),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2XS)
            ) {
                Text(
                    text = menuItem.dish.title,
                    style = MaterialTheme.typeStyle.labelMedium,
                    color = MaterialTheme.colors.contentPrimary
                )
                Text(
                    text = stringResource(R.string.price_format_ea, menuItem.dish.price),
                    style = MaterialTheme.typeStyle.bodyXSmall,
                    color = MaterialTheme.colors.contentPlaceholder
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(top = imageOffset)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.currency_symbol),
                        style = MaterialTheme.typeStyle.bodyMedium,
                        color = MaterialTheme.colors.contentOnAction
                    )
                    Text(
                        stringResource(
                            R.string.price_format,
                            if (menuItem.dish.discountedPrice != null) discountedTotal else orderTotal
                        ),
                        style = MaterialTheme.typeStyle.headlineSmall,
                        color = MaterialTheme.colors.contentOnAction
                    )
                }
                discountPercent?.let {
                    Column(horizontalAlignment = Alignment.End) {

                        Text(
                            stringResource(R.string.currency_symbol) + stringResource(
                                R.string.price_format,
                                orderTotal
                            ),
                            style = MaterialTheme.typeStyle.bodyXSmall.copy(
                                textAlign = TextAlign.End,
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = MaterialTheme.colors.contentPrimary
                        )
                        Spacer(Modifier.height(MaterialTheme.dimens.sizeSM))
                        Tag(
                            stringResource(R.string.discount_format, discountPercent),
                            variant = TagVariant.SuccessLight
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .padding(start = MaterialTheme.dimens.size2XL)
        ) {
            Button(
                stringResource(R.string.act_remove),
                onRemoveItem,
                variant = ButtonVariant.GHOST,
                modifier = Modifier.weight(1f)
            )
            Stepper(
                menuItem.quantity,
                onIncrease = onIncreaseQuantity,
                onDecrease = onDecreaseQuantity
            )
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
                imageURL = "https://images.pexels.com/photos/1108117/pexels-photo-1108117.jpeg",
                stock = 25,
                nutritionInfo = NutritionInfo(150, 1, 2, 3),
                discountedPrice = 8.99,
                category = listOf(),
                dateAdded = LocalDateTime(1999, 12, 30, 11, 11, 11),
                popularityIndex = 11
            )
            OrderItem(MenuItem(dish, 2), {}, {}, {})
            OrderItem(MenuItem(dish.copy(discountedPrice = null), 4), {}, {}, {})
        }
    }
}