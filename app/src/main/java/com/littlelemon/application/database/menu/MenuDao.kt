package com.littlelemon.application.database.menu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.littlelemon.application.database.menu.models.CategoryEntity
import com.littlelemon.application.database.menu.models.DishCategoryCrossRef
import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertDishes(dishes: List<DishEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertDishCategoryCrossRefs(crossRefs: List<DishCategoryCrossRef>)

    @Query("SELECT COUNT(*) FROM dishentity")
    suspend fun getDishCount(): Int

    /**
     * Inserts dishes that have zero or more categories into the database.
     */
    @Transaction
    suspend fun insertDishes(
        dishes: List<DishEntity>,
        categories: List<CategoryEntity>,
        crossRefs: List<DishCategoryCrossRef>
    ) {
        insertCategories(categories)
        insertDishes(dishes)
        insertDishCategoryCrossRefs(crossRefs)
    }

    @Transaction
    @Query("SELECT * FROM dishentity")
    fun getAllDishes(): Flow<List<DishWithCategories>>

    @Query("SELECT DISTINCT(categoryName), categoryId FROM categoryentity")
    fun getAllCategories(): Flow<List<CategoryEntity>>

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