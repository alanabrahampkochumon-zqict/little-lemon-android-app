package com.littlelemon.application.menu.presentation

import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.models.Category

data class CategoryState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: UiText? = null
)