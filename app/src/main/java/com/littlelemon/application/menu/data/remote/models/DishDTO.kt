package com.littlelemon.application.menu.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishDTO(
    val id: String,
    val title: String,
    val description: String?,
    val price: Long,
    val image: String,
    val stock: Int,
    @SerialName("nutrition_info")
    val nutritionInfo: NutritionInfoDTO?,
    @SerialName("discounted_price")
    val discountedPrice: Long,
    @SerialName("popularity_index")
    val popularityIndex: Int,
    val dateAdded: Long,
    @SerialName("categories")
    val categories: List<CategoryDTO>
)