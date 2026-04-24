package com.littlelemon.application.cart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.cart.data.local.models.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract val dao: CartDao
}