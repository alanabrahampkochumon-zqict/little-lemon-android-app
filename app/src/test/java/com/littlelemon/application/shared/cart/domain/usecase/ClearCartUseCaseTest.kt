package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
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
            CartDetailItem(dish, Random.nextInt(3, 5))
        }
        repository = FakeCartRepository(initialItems)
        useCase = ClearCartUseCase(repository)

        // Initial assertion
        val initialData = repository.getAllDetailedCartItems().first()
        assertEquals(numItems, initialData.size)

        // When use case is invoked
        useCase()

        // Then, the repository is cleared
        val finalData = repository.getAllDetailedCartItems().first()
        assertEquals(0, finalData.size)
    }

}