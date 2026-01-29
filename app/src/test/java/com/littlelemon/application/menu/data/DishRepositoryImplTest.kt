package com.littlelemon.application.menu.data

import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting

class DishRepositoryImplTest() : DishRepository {
    override suspend fun getDishes(
        max: Int,
        sorting: DishSorting,
        filter: DishFilter,
        fetchFromRemote: Boolean
    ) {
        TODO("Not yet implemented")
    }

}