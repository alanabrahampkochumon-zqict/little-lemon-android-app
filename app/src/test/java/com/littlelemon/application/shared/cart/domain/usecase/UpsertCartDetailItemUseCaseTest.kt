package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertContains

class UpsertCartDetailItemUseCaseTest {

    private lateinit var repository: CartRepository

    private lateinit var useCase: UpsertCartItemUseCase

    @Test
    fun repositorySuccess_updatesData() = runTest {
        // Given repository success
        val dish = DishGenerator.generateDish()
        val cartDetailItem = CartDetailItem(dish, Random.nextInt(3, 5))
        repository = FakeCartRepository(emptyList())
        useCase = UpsertCartItemUseCase(repository)

        // When cart item is upserted
        useCase(cartDetailItem)

        // Then, the item is added to the repo
        val internalList = repository.getAllDetailedCartItems().first()
        assertContains(internalList, cartDetailItem)
    }
}