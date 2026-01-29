package com.littlelemon.application.menu.data.local.dao

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
    @Query("SELECT * FROM dishentity")
    fun getAllDishes(): Flow<List<DishWithCategories>>


    @Transaction
    @Query("SELECT * FROM dishentity ORDER BY popularityIndex DESC")
    fun getDishesSortedByPopularity(): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM dishentity ORDER BY " +
                "CASE WHEN :ascending = 1 THEN title END ASC," +
                "CASE WHEN :ascending = 0 THEN title END DESC"
    )
    fun getDishesSortedByName(ascending: Boolean = true): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM dishentity ORDER BY " +
                "CASE WHEN :ascending = 1 THEN price END ASC," +
                "CASE WHEN :ascending = 0 THEN price END DESC"
    )
    fun getDishesSortedByPrice(ascending: Boolean = true): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM dishentity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN dateAdded END ASC, " +
                " CASE WHEN :ascending = 0 THEN dateAdded END DESC "
    )
    fun getDishesSortedByAdded(
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM DishEntity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN calories END ASC, " +
                " CASE WHEN :ascending = 0 THEN calories END DESC "
    )
    fun getDishesSortedByCalories(
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query(
        "SELECT * FROM DishEntity ORDER BY " +
                " CASE WHEN :ascending = 1 THEN protein END ASC, " +
                " CASE WHEN :ascending = 0 THEN protein END DESC "
    )
    fun getDishesSortedByProtein(
        ascending: Boolean = true
    ): Flow<List<DishWithCategories>>

    @Transaction
    @Query("DELETE FROM dishentity")
    suspend fun deleteAllDishes()
}