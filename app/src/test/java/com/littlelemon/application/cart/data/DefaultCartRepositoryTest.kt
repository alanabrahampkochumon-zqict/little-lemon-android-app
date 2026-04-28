package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.data.remote.FakeCartRemoteDataSource
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.cart.FakeCartDao
import com.littlelemon.application.database.cart.mappers.toDTO
import com.littlelemon.application.database.cart.mappers.toEntity
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random


class DefaultCartRepositoryTest {

    private lateinit var remoteDS: CartRemoteDataSource
    private lateinit var localDS: CartDao
    private lateinit var repository: CartRepository


    private val cartItems = List(5) {
        CartItem(DishGenerator.generateDish(), Random.nextInt(5, 10))
    }

    @BeforeEach
    fun setUp() {
        remoteDS = FakeCartRemoteDataSource(cartItems.map { it.toDTO() })
        localDS = FakeCartDao(cartItems.map { it.toEntity() })

        repository = DefaultCartRepository(remoteDS, localDS)
    }

    @Nested
    inner class UpdateCartItem {

        @Test
        fun success_updatesItemLocallyAndInRemote() = runTest {
            val cartItem = CartItem(DishGenerator.generateDish(), 5)
            repository.upsertCartItem(cartItem)
        }

        @Test
        fun remoteFailure_rollbackItem() = runTest {
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            repository.upsertCartItem(cartItems.first().copy(quantity = 100))
        }

        @Test
        fun localFailure_rethrowsError() = runTest {
            localDS = FakeCartDao(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            assertThrows<IllegalArgumentException> {
                repository.upsertCartItem(
                    cartItems.first().copy(quantity = 100)
                )
            }
        }
    }


}