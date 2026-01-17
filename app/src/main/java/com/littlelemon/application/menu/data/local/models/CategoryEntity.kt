package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val categoryName: String
)