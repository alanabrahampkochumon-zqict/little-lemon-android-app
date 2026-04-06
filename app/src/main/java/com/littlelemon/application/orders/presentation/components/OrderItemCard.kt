package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.DashedDivider
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.orders.domain.models.MenuItem
import com.littlelemon.application.orders.domain.models.OrderItem
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

@Composable
fun OrderCardItem(
    orderItem: OrderItem,
    onReorder: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false
) {

    val shape = MaterialTheme.shapes.large
    val shadow = MaterialTheme.shadows.dropMD
    val density = LocalDensity.current.density
    val tagVariant = if (expanded) {
        when (orderItem.orderStatus) {
            OrderItem.OrderStatus.Delivered -> TagVariant.SuccessFilled
            OrderItem.OrderStatus.Cancelled -> TagVariant.ErrorLight
        }
    } else {
        TagVariant.SuccessLight
    }

    val dateFormat = LocalDateTime.Format {
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        day()
        char(',')
        char(' ')
        year()
    }
    Column(
        modifier = modifier
            .dropShadow(shape, shadow.firstShadow.toComposeShadow(density))
            .dropShadow(shape, shadow.secondShadow?.toComposeShadow(density) ?: Shadow(0.dp))
            .background(MaterialTheme.colors.primary, shape = shape)
            .padding(top = MaterialTheme.dimens.sizeXL)
            .clip(shape)
    ) {
        Column(Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = orderItem.orderName,
                    style = MaterialTheme.typeStyle.headlineSmall,
                    color = MaterialTheme.colors.contentPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
                if (expanded) {
                    Tag(orderItem.orderStatus.name, variant = tagVariant)
                } else {
                    Tag(
                        stringResource(R.string.currency_symbol) +
                                stringResource(R.string.price_format, orderItem.billAmount),
                        variant = tagVariant
                    )
                }
            }
            Spacer(Modifier.height(MaterialTheme.dimens.size2XS))
            Text(
                orderItem.orderDate.format(dateFormat),
                style = MaterialTheme.typeStyle.bodyXSmall,
                color = MaterialTheme.colors.contentPlaceholder
            )
        }
        Spacer(Modifier.height(MaterialTheme.dimens.sizeMD))
        Column(
            Modifier.padding(
                start = MaterialTheme.dimens.sizeXL,
                end = MaterialTheme.dimens.sizeXL,
                bottom = MaterialTheme.dimens.sizeSM
            )
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.secondary, shape = MaterialTheme.shapes.small),
                contentPadding = PaddingValues(
                    MaterialTheme.dimens.sizeMD
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.sizeMD
                )
            ) {
                items(orderItem.menuItems) { menuItem ->
                    DishImageCard(
                        imageURL = menuItem.dish.imageURL,
                        dishName = menuItem.dish.title,
                        quantity = menuItem.quantity,
                    )
                }
            }
            if (expanded) {
                Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                DashedDivider()
                Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                orderItem.specialInstructions?.let { specialInstruction ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colors.secondary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(
                                horizontal = MaterialTheme.dimens.sizeLG,
                                vertical = MaterialTheme.dimens.sizeMD
                            ),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM)
                    ) {
                        Text(
                            stringResource(R.string.special_instructions),
                            style = MaterialTheme.typeStyle.labelSmall,
                            color = MaterialTheme.colors.contentTertiary
                        )
                        Text(
                            specialInstruction,
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentTertiary,
                            maxLines = 1,
                        )
                    }
                    Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                    DashedDivider()
                    Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(
                            horizontal = MaterialTheme.dimens.sizeLG,
                            vertical = MaterialTheme.dimens.sizeMD
                        ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                        Text(
                            stringResource(R.string.payment_mode),
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentTertiary
                        )
                        Text(
                            orderItem.paymentMode,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typeStyle.labelSmall.copy(textAlign = TextAlign.End),
                            color = MaterialTheme.colors.contentTertiary,
                            maxLines = 1,
                        )
                    }
                    if (orderItem.orderStatus == OrderItem.OrderStatus.Delivered) {
                        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                            Text(
                                stringResource(R.string.delivered_to),
                                style = MaterialTheme.typeStyle.bodySmall,
                                color = MaterialTheme.colors.contentTertiary
                            )
                            Text(
                                orderItem.deliveryAddressLabel,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typeStyle.labelSmall.copy(textAlign = TextAlign.End),
                                color = MaterialTheme.colors.contentTertiary,
                                maxLines = 1,
                            )
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                            Text(
                                stringResource(R.string.refund_made_on),
                                style = MaterialTheme.typeStyle.bodySmall,
                                color = MaterialTheme.colors.contentTertiary
                            )
                            Text(
                                orderItem.refundDate!!.format(dateFormat),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typeStyle.labelSmall.copy(textAlign = TextAlign.End),
                                color = MaterialTheme.colors.contentTertiary,
                                maxLines = 1,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(
                            horizontal = MaterialTheme.dimens.sizeLG,
                            vertical = MaterialTheme.dimens.sizeMD
                        ),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                        Text(
                            stringResource(R.string.bill_amount),
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentTertiary
                        )
                        Text(
                            stringResource(R.string.currency_symbol) +
                                    stringResource(R.string.price_format, orderItem.billAmount),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typeStyle.labelSmall.copy(textAlign = TextAlign.End),
                            color = MaterialTheme.colors.contentTertiary,
                            maxLines = 1,
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeSM))
                    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                        Text(
                            stringResource(R.string.delivered_to),
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentTertiary
                        )
                        Text(
                            stringResource(R.string.currency_symbol) +
                                    stringResource(R.string.price_format, orderItem.deliveryCharge),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typeStyle.labelSmall.copy(textAlign = TextAlign.End),
                            color = MaterialTheme.colors.contentTertiary,
                            maxLines = 1,
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
                    HorizontalDivider(color = MaterialTheme.colors.outlineSecondary)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
                    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD)) {
                        Text(
                            stringResource(R.string.total_amount),
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentTertiary
                        )
                        Text(
                            stringResource(R.string.currency_symbol) +
                                    stringResource(R.string.price_format, orderItem.totalAmount),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typeStyle.labelLarge.copy(textAlign = TextAlign.End),
                            color = MaterialTheme.colors.contentPrimary,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeLG))
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.highlight)
                .padding(
                    horizontal = MaterialTheme.dimens.sizeXL,
                    vertical = MaterialTheme.dimens.sizeLG
                )
                .fillMaxWidth()
                .clickable(onClick = onReorder),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.reorder),
                color = MaterialTheme.colors.contentOnColor,
                style = MaterialTheme.typeStyle.labelMedium
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xf5f5f6, heightDp = 1200)
@Composable
private fun OrderCardItemPreview() {
    val orderItem = OrderItem(
        orderName = "Greek Salad & Bruschetta",
        orderStatus = OrderItem.OrderStatus.Delivered,
        menuItems = listOf(
            MenuItem(
                Dish(
                    title = "Dish 1",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 5
            ),
            MenuItem(
                Dish(
                    title = "Dish 2",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 10
            ), MenuItem(
                Dish(
                    title = "Dish 3",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 1
            ), MenuItem(
                Dish(
                    title = "Dish 4",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 1
            )
        ),
        orderDate = LocalDateTime(2025, 12, 30, 12, 30),
        specialInstructions = "Avoid using coconut milk",
        paymentMode = "Card ending in 3521",
        deliveryAddressLabel = "Work Address",
        billAmount = 55.45,
        deliveryCharge = 0.0,
        totalAmount = 55.45,
        refundDate = null
    )
    LittleLemonTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrderCardItem(orderItem, {}, expanded = true)
            OrderCardItem(orderItem, {}, expanded = false)
            OrderCardItem(
                orderItem.copy(
                    orderStatus = OrderItem.OrderStatus.Cancelled,
                    refundDate = LocalDateTime(2025, 12, 30, 13, 0)
                ), {}, expanded = true
            )
        }
    }
}