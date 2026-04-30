package com.littlelemon.application.orders.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
    val id: String?,
    val label: String,
    @SerialName("order_date")
    val orderDate: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    val status: String,
    @SerialName("payment_mode")
    val paymentMode: String,
    @SerialName("deliver_to")
    val deliverTo: String,
    @SerialName("bill_amount")
    val billAmount: Int,
    @SerialName("delivery_charge")
    val deliveryCharge: Int,
    @SerialName("discount")
    val discount: Int,
    @SerialName("total_payable")
    val totalPayable: Int,
    @SerialName("refunded_on")
    val refundedOn: String?,
    @SerialName("special_instructions")
    val specialInstructions: String,
    @SerialName("order_items")
    val orderItems: List<OrderItem>
) {
    @Serializable
    data class OrderItem(
        @SerialName("dish_id")
        val dishId: String,
        @SerialName("dish_name_snapshot")
        val dishName: String,
        @SerialName("dish_price_snapshot")
        val dishPrice: Int,
        @SerialName("order_quantity")
        val orderQuantity: Int
    )
}