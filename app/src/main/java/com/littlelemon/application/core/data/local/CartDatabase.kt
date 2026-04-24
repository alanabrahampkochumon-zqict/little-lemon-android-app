package com.littlelemon.application.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.core.data.local.models.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract val dao: CartDao
}