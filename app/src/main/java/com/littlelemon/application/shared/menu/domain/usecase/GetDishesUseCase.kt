package com.littlelemon.application.shared.menu.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.menu.domain.MenuRepository
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.util.DishFilter
import com.littlelemon.application.shared.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.Flow

class GetDishesUseCase(
    private val repository: MenuRepository
) {
    operator fun invoke(
        sorting: DishSorting = DishSorting.POPULARITY,
        filter: DishFilter? = null,
        forceFetch: Boolean = false,
        filterCategory: String? = null
    ): Flow<Resource<List<Dish>>> =
        repository.getDishes(sorting, filter, forceFetch, filterCategory)
}