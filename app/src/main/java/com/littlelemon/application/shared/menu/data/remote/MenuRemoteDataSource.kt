package com.littlelemon.application.shared.menu.data.remote

import com.littlelemon.application.shared.menu.data.remote.models.DishDTO

interface MenuRemoteDataSource {

    suspend fun fetchDishes(): List<DishDTO>
}