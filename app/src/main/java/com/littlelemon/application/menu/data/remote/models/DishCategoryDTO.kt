package com.littlelemon.application.menu.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishCategoryDTO(
    @SerialName("dish_id")
    val dishId: String,
    @SerialName("category_id")
    val categoryId: String
)
