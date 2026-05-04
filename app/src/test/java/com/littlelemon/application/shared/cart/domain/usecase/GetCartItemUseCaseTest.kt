package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.cart.domain.models.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class GetCartItemUseCaseTest {
    private lateinit var repository: CartRepository

    private lateinit var useCase: GetCartItemUseCase

    @Test
    fun repositorySuccess_emitsData() = runTest {
        val cartDetailItems = List(3) {
            val dish = DishGenerator.generateDish()
            CartDetailItem(dish, Random.nextInt(3, 5))
        }
        val cartItems = cartDetailItems.map { CartItem(it.dish.id, it.quantity) }


        repository = FakeCartRepository(cartDetailItems)
        useCase = GetCartItemUseCase(repository)

        val retrievedItems = useCase().first()
        assertEquals(cartItems, retrievedItems)
    }

    @Test
    fun repositoryFailure_emitsEmptyList() = runTest {
        repository = FakeCartRepository(throwError = true)
        useCase = GetCartItemUseCase(repository)

        val retrievedItems = useCase().first()
        assertEquals(0, retrievedItems.size)
    }
}