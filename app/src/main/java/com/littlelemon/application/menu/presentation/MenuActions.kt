package com.littlelemon.application.menu.presentation

import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting

sealed interface MenuActions {
    data class FetchDishes(val fromRemote: Boolean) : MenuActions
    data class ApplySorting(val sorting: DishSorting) : MenuActions
    data class ApplyFiltering(val filter: DishFilter? = null) : MenuActions
}