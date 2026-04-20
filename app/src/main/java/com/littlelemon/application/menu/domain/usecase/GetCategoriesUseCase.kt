package com.littlelemon.application.menu.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.models.Category
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: MenuRepository
) {
    operator fun invoke(): Flow<Resource<List<Category>>> = repository.getAllCategories()
}