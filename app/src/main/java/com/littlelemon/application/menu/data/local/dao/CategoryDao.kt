package com.littlelemon.application.menu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.littlelemon.application.menu.data.local.models.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categoryentity")
    suspend fun getAllCategory(): List<CategoryEntity>

    @Query("SELECT * FROM categoryentity WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity

    @Query("DELETE FROM categoryentity")
    suspend fun deleteAllCategories()
}