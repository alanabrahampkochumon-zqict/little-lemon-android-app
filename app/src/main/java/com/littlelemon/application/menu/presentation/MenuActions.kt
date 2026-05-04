package com.littlelemon.application.menu.presentation

import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.util.DishFilter
import com.littlelemon.application.shared.menu.domain.util.DishSorting

sealed interface MenuActions {
    data class FetchDishes(val fromRemote: Boolean) : MenuActions
    data class ApplySorting(val sorting: DishSorting) : MenuActions
    data class ApplyFiltering(val filter: DishFilter? = null) : MenuActions

    data class AddToCart(val dish: Dish) : MenuActions
    data class RemoveFromCart(val dish: Dish) : MenuActions

    data class UpdateDishCategory(val category: String? = null) : MenuActions
}