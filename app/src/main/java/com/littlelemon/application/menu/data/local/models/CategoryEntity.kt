package com.littlelemon.application.menu.data.local.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CategoryEntity(
    @param:NonNull @PrimaryKey(autoGenerate = false) val categoryId: String = UUID.randomUUID()
        .toString(),
    val categoryName: String
)