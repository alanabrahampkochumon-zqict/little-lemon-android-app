package com.littlelemon.application.shared.menu.domain

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.menu.domain.models.Category
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.util.DishFilter
import com.littlelemon.application.shared.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    fun getDishes(
        sorting: DishSorting = DishSorting.POPULARITY,
        filter: DishFilter? = null,
        fetchFromRemote: Boolean = false,
        filterCategory: String? = null,
    ): Flow<Resource<List<Dish>>>


    fun getAllCategories(): Flow<Resource<List<Category>>>
}