package com.littlelemon.application.orders.domain.models

import kotlinx.datetime.LocalDateTime

data class OrderItem(
    val orderName: String,
    val orderStatus: OrderStatus,
    val menuItems: List<MenuItem>,
    val orderDate: LocalDateTime,
    val specialInstructions: String?,
    val paymentMode: String,
    val deliveryAddressLabel: String,
    val billAmount: Double,
    val deliveryCharge: Double,
    val totalAmount: Double,
) {
    enum class OrderStatus {
        Delivered,
        Cancelled
    }
}