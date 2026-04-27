package com.littlelemon.application.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.database.address.dao.AddressDao
import com.littlelemon.application.database.address.dao.GeocodingDao
import com.littlelemon.application.database.address.models.AddressEntity
import com.littlelemon.application.database.address.models.GeocodingEntity
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.MenuDao
import com.littlelemon.application.database.menu.models.CategoryEntity
import com.littlelemon.application.database.menu.models.DishCategoryCrossRef
import com.littlelemon.application.database.menu.models.DishEntity


@Database(
    entities = [AddressEntity::class, GeocodingEntity::class, CartItemEntity::class, CategoryEntity::class, DishEntity::class, DishCategoryCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class LittleLemonDatabase : RoomDatabase() {

    abstract val addressDao: AddressDao
    abstract val geocodingDao: GeocodingDao

    abstract val cartDao: CartDao

    abstract val menuDao: MenuDao
}