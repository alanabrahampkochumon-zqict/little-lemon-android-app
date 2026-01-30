package com.littlelemon.application.menu.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    @SerialName("id")
    val categoryId: String,
    @SerialName("category_name")
    val categoryName: String
)