package com.littlelemon.application.menu.presentation

import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting

sealed interface DishActions {
    data class FetchDishes(val fromRemote: Boolean) : DishActions
    data class ApplySorting(val sorting: DishSorting) : DishActions
    data class ApplyFiltering(val filter: DishFilter? = null) : DishActions
}