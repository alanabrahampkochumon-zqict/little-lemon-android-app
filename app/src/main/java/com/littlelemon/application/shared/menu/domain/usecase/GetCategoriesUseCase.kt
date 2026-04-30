package com.littlelemon.application.shared.menu.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.menu.domain.MenuRepository
import com.littlelemon.application.shared.menu.domain.models.Category
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: MenuRepository
) {
    operator fun invoke(): Flow<Resource<List<Category>>> = repository.getAllCategories()
}