package com.littlelemon.application.menu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity

@Database(
    entities = [CategoryEntity::class, DishEntity::class, DishCategoryCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class MenuDatabase : RoomDatabase() {
    abstract val dao: DishDao
}