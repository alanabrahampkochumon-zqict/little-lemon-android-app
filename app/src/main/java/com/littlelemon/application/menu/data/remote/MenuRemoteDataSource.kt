package com.littlelemon.application.menu.data.remote

import com.littlelemon.application.menu.data.remote.models.DishDTO

interface MenuRemoteDataSource {

    suspend fun fetchDishes(): List<DishDTO>;
}