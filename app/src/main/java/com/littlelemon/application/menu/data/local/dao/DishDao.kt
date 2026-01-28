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
interface DishDao {

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
            Log.d("Testing", "DISHID: ${dishId}, CAT: $catId")
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
    @Query("SELECT * FROM dishentity ORDER BY popularityIndex DESC LIMIT :limit")
    fun getDishesSortedByPopularity(limit: Int = 10): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM dishentity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN dateAdded END ASC, " +
                " CASE WHEN :ascending = 0 THEN dateAdded END DESC " +
                " LIMIT :limit"
    )
    fun getDishesSortedByAdded(
        limit: Int = 10,
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM DishEntity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN calories END ASC, " +
                " CASE WHEN :ascending = 0 THEN calories END DESC " +
                " LIMIT :limit"
    )
    fun getDishesSortedByCalories(
        limit: Int = 10,
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM DishEntity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN protein END ASC, " +
                " CASE WHEN :ascending = 0 THEN protein END DESC " +
                " LIMIT :limit"
    )
    fun getDishesSortedByProtein(
        limit: Int = 10,
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query("DELETE FROM dishentity")
    suspend fun deleteAllDishes()
}