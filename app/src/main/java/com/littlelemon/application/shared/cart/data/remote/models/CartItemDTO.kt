package com.littlelemon.application.shared.cart.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemDTO(
    @SerialName("dish_id")
    val dishId: String,
    val quantity: Int
)