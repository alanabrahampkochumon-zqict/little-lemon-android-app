package com.littlelemon.application.cart.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    @SerialName("dish_id")
    val dishId: String,
    val quantity: Int
)