package com.littlelemon.application.shared.orders.domain.models

data class CartItem(
    val menuItemItem: List<MenuItem>,
    val subtotal: Double,
    val taxes: Double,
    val discount: Double,
    val shippingFee: Double,
    val total: Double,
)
