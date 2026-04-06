package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.orders.domain.models.OrderItem
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

// TODO: Order
@Composable
fun OrderCardItem(orderItem: OrderItem, modifier: Modifier = Modifier, expanded: Boolean = false) {

    val shape = MaterialTheme.shapes.large
    val shadow = MaterialTheme.shadows.dropMD
    val density = LocalDensity.current.density
    val tagVariant = if (expanded) {
        when (orderItem.orderStatus) {
            OrderItem.OrderStatus.Delivered -> TagVariant.SuccessFilled
            OrderItem.OrderStatus.Cancelled -> TagVariant.DisabledFilled
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
    ) {
        Column(Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = orderItem.orderName,
                    style = MaterialTheme.typeStyle.headlineSmall,
                    color = MaterialTheme.colors.contentPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
                Tag(orderItem.orderStatus.name, variant = tagVariant)
            }
            Spacer(Modifier.height(MaterialTheme.dimens.size2XS))
            Text(orderItem.orderDate.format(dateFormat), style = MaterialTheme.typeStyle.bodyXSmall, color = MaterialTheme.colors.contentPlaceholder)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xf5f5f6)
@Composable
private fun OrderCardItemPreview() {
    val orderItem = OrderItem(
        orderName = "Greek Salad & Bruschetta",
        orderStatus = OrderItem.OrderStatus.Delivered,
        menuItems = listOf(),
        orderDate = LocalDateTime(2025, 12, 30, 12, 30),
        specialInstructions = "Avoid using coconut milk",
        paymentMode = "Card ending in 3521",
        deliveryAddressLabel = "Work Address",
        billAmount = 55.45,
        deliveryCharge = 0.0,
        totalAmount = 55.45
    )
    LittleLemonTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrderCardItem(orderItem, expanded = true)
            OrderCardItem(orderItem, expanded = false)
        }
    }
}