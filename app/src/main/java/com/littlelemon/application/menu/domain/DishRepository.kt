package com.littlelemon.application.menu.domain

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.Flow

interface DishRepository {

    suspend fun getDishes(
        sorting: DishSorting = DishSorting.POPULARITY,
        filter: DishFilter? = null,
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Dish>>>

}