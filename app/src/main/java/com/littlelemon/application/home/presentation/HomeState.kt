package com.littlelemon.application.home.presentation

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish


data class HomeState(
    val addresses: List<LocalAddress> = emptyList(),
    val addressLoading: Boolean = false,
    val addressError: UiText? = null,

    val popularDishes: List<Dish> = emptyList(),
    val dishLoading: Boolean = false,
    val dishError: UiText? = null,

    val categories: List<Category> = emptyList(),
    val categoryLoading: Boolean = false,
    val categoryError: UiText? = null,
)