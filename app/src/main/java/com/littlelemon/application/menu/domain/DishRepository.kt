package com.littlelemon.application.menu.domain

import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting

interface DishRepository {

    suspend fun getDishes(
        max: Int = 10,
        sorting: DishSorting = DishSorting.BEST_SELLING,
        filter: DishFilter = DishFilter.INCLUDE_OUT_OF_STOCK
    )

    suspend fun getDishDetail(
        dishId: Int
    )
}