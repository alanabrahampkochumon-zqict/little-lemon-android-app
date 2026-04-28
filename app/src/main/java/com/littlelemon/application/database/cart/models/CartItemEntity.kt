package com.littlelemon.application.database.cart.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Entity
data class CartItemEntity(
    @PrimaryKey val dishId: String,
    val quantity: Int,
)