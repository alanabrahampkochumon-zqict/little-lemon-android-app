package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.orders.domain.models.OrderItem
import kotlinx.datetime.LocalDateTime

// TODO: Order
@Composable
fun OrderCardItem(orderItem: OrderItem, modifier: Modifier = Modifier, expanded: Boolean = false) {

}

@Preview
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