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

class GetCartItemsUseCaseTest {


    private lateinit var repository: CartRepository

    private lateinit var useCase: GetCartItemsUseCase

    @Test
    fun repositorySuccess_emitsData() = runTest {
        val initialItems = List(3) {
            val dish = DishGenerator.generateDish()
            CartItem(dish, Random.nextInt(3, 5))
        }

        repository = FakeCartRepository(initialItems)
        useCase = GetCartItemsUseCase(repository)

        val retrievedItems = useCase().first()
        assertEquals(initialItems, retrievedItems)
    }

    @Test
    fun repositoryFailure_emitsEmptyList() = runTest {
        repository = FakeCartRepository(throwError = true)
        useCase = GetCartItemsUseCase(repository)

        val retrievedItems = useCase().first()
        assertEquals(0, retrievedItems.size)
    }

}