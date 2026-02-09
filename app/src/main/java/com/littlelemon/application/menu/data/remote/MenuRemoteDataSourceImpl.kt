package com.littlelemon.application.menu.data.remote

import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.menu.data.remote.models.DishDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.client.plugins.HttpRequestTimeoutException

class MenuRemoteDataSourceImpl(
    private val client: SupabaseClient
) : MenuRemoteDataSource {

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun fetchDishes(): List<DishDTO> {
        return client.from(SupabaseTables.DISH)
            .select(Columns.raw("id, title, description, ${SupabaseTables.DISH_CATEGORY}(category_name), ${SupabaseTables.NUTRITION_INFO}(calories, protein, carbs, fats)")) //
            .decodeList<DishDTO>()
    }
}