package com.littlelemon.application.cart.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity
data class CartItemEntity(
    val dishId: String,
    val quantity: Int,
    @PrimaryKey val id: String = Uuid.generateV4().toString(),
)