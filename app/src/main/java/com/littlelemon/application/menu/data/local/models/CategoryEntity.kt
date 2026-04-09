package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false) val categoryId: String = UUID.randomUUID()
        .toString(),
    val categoryName: String
)