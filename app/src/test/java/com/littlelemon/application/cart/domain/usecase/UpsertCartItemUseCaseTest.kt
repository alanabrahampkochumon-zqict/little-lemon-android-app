package com.littlelemon.application.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertContains

class UpsertCartItemUseCaseTest {

    private lateinit var repository: CartRepository

    private lateinit var useCase: UpsertCartItemUseCase

    @Test
    fun repositorySuccess_updatesData() = runTest {
        // Given repository success
        val dish = DishGenerator.generateDish()
        val cartItem = CartItem(dish, Random.nextInt(3, 5))
        repository = FakeCartRepository(emptyList())
        useCase = UpsertCartItemUseCase(repository)

        // When cart item is upserted
        useCase(cartItem)

        // Then, the item is added to the repo
        val internalList = repository.getAllCartItems().first()
        assertContains(internalList, cartItem)
    }
}