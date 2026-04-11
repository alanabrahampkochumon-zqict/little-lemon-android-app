package com.littlelemon.application.menu.data.remote

import android.util.Log
import com.littlelemon.application.BuildConfig
import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.menu.data.remote.models.DishDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
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
            .select(Columns.raw("id, title, description, price, discounted_price, stock, popularity_index, date_added, ${SupabaseTables.DISH_CATEGORY}(id, category_name), ${SupabaseTables.NUTRITION_INFO}(calories, protein, carbs, fats), image"))
            .decodeList<DishDTO>().map { dishDTO ->
                val fullURL =
                    client.storage.from(BuildConfig.SUPABASE_BUCKET_NAME).publicUrl(dishDTO.image)
                Log.d("Image", fullURL)
                dishDTO.copy(image = fullURL)
            }
    }
}