package com.littlelemon.application.menu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.littlelemon.application.menu.data.local.models.DishEntity

@Dao
interface DishDao {

    @Insert
    suspend fun insertDish(dish: DishEntity)

    @Query("SELECT * FROM dishentity LIMIT :limit")
    suspend fun getDishes(limit: Int = 10)

    @Query("DELETE FROM dishentity")
    suspend fun deleteAllDishes()
}