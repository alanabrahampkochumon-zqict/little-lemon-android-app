package com.littlelemon.application.menu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.littlelemon.application.menu.data.local.models.NutritionInfoEntity

@Dao
interface NutritionInfoDao {

    @Insert
    suspend fun insertNutritionInfo(nutritionInfo: NutritionInfoEntity)

    @Query("SELECT * FROM nutritioninfoentity WHERE id = :id")
    suspend fun getNutritionInfo(id: Int): NutritionInfoEntity

    @Query("DELETE FROM nutritioninfoentity")
    suspend fun deleteAllNutritionalInfo()
}