package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.data.remote.models.DishDTO
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class FakeDishRemoteDataSource(
    private val throwError: Boolean,
    private val defaultDishes: List<DishDTO>? = null
) : MenuRemoteDataSource {

    private val dishes: MutableList<DishDTO> = mutableListOf()

    init {
        if (defaultDishes != null) {
            dishes.addAll(defaultDishes)
        } else {

            val generator = MenuDTOGenerator()
            repeat(20) {
                dishes.add(generator.generateDishDTO())
            }
        }
    }

    override suspend fun fetchDishes(): List<DishDTO> {
        if (throwError) {
            val mockClient = HttpClient(MockEngine) {
                engine {
                    addHandler {
                        respond(
                            content = ByteReadChannel("Fake response data"),
                            status = HttpStatusCode.BadRequest,
                            headers = headersOf("Content-Type", "text/plain")
                        )
                    }
                }
            }
            throw RestException(
                error = "Fake Error",
                description = "Some description",
                response = mockClient.get("")
            )
        }
        return dishes
    }
}