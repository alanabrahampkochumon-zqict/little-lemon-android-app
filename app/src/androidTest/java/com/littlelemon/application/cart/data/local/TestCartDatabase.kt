package com.littlelemon.application.cart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.cart.data.local.models.CartItemEntity
import com.littlelemon.application.menu.data.local.dao.MenuDao
import com.littlelemon.application.menu.data.local.models.DishEntity

@Database(entities = [CartItemEntity::class, DishEntity::class], version = 1)
abstract class TestCartDatabase : RoomDatabase() {
    abstract val cartDao: CartDao
    abstract val menuDao: MenuDao
}