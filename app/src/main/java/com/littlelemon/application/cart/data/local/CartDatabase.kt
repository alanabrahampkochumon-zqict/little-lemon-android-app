package com.littlelemon.application.cart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.cart.data.local.models.CartItemEntity
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity

@Database(
    entities = [CartItemEntity::class, DishEntity::class, CategoryEntity::class, DishCategoryCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class CartDatabase : RoomDatabase() {
    abstract val dao: CartDao
}