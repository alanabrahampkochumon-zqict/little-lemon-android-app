package com.littlelemon.application.menu.data.remote

import com.littlelemon.application.menu.data.remote.models.DishDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class MenuRemoteDataSource(
    private val client: SupabaseClient
) {

    suspend fun fetchDishes(): List<DishDTO> {
        return client.from("dish")
            .select(Columns.raw("id, title, description, category(category_name), nutrition_info(calories, protein, carbs, fats)")) //
            .decodeList<DishDTO>()
    }
}