package com.littlelemon.application.menu.data.local.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface DishWithCategoriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDish(dish: DishEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDishCategoryCrossRef(crossRef: DishCategoryCrossRef)

    /**
     * Inserts dishes that have zero or more categories into the database.
     */
    @Transaction
    suspend fun insertDish(dishWithCategories: DishWithCategories) {
        val dishId = insertDish(dishWithCategories.dish)

        for (category in dishWithCategories.categories) {
            val catId = insertCategory(category)
            Log.d("Testing", "DISHID: ${dishId}, CAT: ${catId}")
            val crossRef = DishCategoryCrossRef(
                dishId = dishId,
                categoryId = catId
            )
            insertDishCategoryCrossRef(
                crossRef
            )
        }

    }

    @Transaction
    @Query("SELECT * FROM dishentity LIMIT :limit")
    fun getAllDishes(limit: Int = 10): Flow<List<DishWithCategories>>

    @Transaction
    @Query("DELETE FROM dishentity")
    suspend fun deleteAllDishes()
}