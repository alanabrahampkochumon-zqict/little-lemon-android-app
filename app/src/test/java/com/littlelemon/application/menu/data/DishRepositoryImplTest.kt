package com.littlelemon.application.menu.data

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.Flow

class DishRepositoryImplTest() : DishRepository {
    override suspend fun getDishes(
        sorting: DishSorting,
        filter: DishFilter,
        fetchFromRemote: Boolean
    ): Flow<Resource<Dish>> {
        TODO("Not yet implemented")
    }

}