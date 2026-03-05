package com.littlelemon.application.menu.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.Flow

class GetDishesUseCase(
    private val repository: DishRepository
) {
    operator fun invoke(
        sorting: DishSorting = DishSorting.POPULARITY,
        filter: DishFilter? = null,
        forceFetch: Boolean = false
    ): Flow<Resource<List<Dish>>> = repository.getDishes(sorting, filter, forceFetch)
}