package com.littlelemon.application.menu.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    @SerialName("category_name")
    val categoryName: String
)