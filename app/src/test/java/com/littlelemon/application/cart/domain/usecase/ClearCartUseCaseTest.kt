package com.littlelemon.application.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class ClearCartUseCaseTest {

    private lateinit var repository: CartRepository

    private lateinit var useCase: ClearCartUseCase

    @Test
    fun repositorySuccess_emitsData() = runTest {
        // Given a non-empty repository
        val numItems = 3
        val initialItems = List(numItems) {
            val dish = DishGenerator.generateDish()
            CartItem(dish, Random.nextInt(3, 5))
        }
        repository = FakeCartRepository(initialItems)
        useCase = ClearCartUseCase(repository)

        // Initial assertion
        val initialData = repository.getAllCartItems().first()
        assertEquals(numItems, initialData.size)

        // When use case is invoked
        useCase()

        // Then, the repository is cleared
        val finalData = repository.getAllCartItems().first()
        assertEquals(0, finalData.size)
    }

}