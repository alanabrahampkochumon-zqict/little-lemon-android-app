package com.littlelemon.application.shared.cart.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartSummaryDTO(
    val subtotal: Int,
    val taxes: Int,
    val discount: Int,
    val shipping: Int,
    @SerialName("total_amount")
    val totalAmount: Int
)