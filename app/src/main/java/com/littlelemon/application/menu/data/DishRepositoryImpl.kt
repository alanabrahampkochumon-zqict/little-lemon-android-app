package com.littlelemon.application.menu.data

import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting

class DishRepositoryImpl(
    private val localDataSource: DishDao,
    private val remoteDataSource: MenuRemoteDataSource
) : DishRepository {

    override suspend fun getDishes(
        max: Int,
        sorting: DishSorting,
        filter: DishFilter,
        fetchFromRemote: Boolean
    ) {
        TODO("Not yet implemented")
    }
}