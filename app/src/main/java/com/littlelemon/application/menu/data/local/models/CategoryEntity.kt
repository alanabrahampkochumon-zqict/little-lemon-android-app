package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long? = null,
    val categoryName: String
)