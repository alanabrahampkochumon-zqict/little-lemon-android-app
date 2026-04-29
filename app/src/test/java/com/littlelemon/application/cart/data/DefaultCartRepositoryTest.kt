package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.data.remote.FakeCartRemoteDataSource
import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.cart.FakeCartDao
import com.littlelemon.application.database.cart.mappers.toDTO
import com.littlelemon.application.database.cart.mappers.toEntity
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExtendWith(StandardTestDispatcherRule::class)
class DefaultCartRepositoryTest {

    private lateinit var remoteDS: CartRemoteDataSource
    private lateinit var localDS: CartDao
    private lateinit var repository: CartRepository


    private val cartItems = List(1) {
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

            // When an item is upserted into the repository
            repository.upsertCartItem(cartItem)


            // Then, the getAllCartItems flow emits success with new item
            val upsertedItem = repository.getAllCartItems().first()
            // Testing with direct comparison will require modifying the fakeDao to pass in both the Dish
            // and CartItem so, a simpler approach is to ensure that the item exists, and it has the right
            // quantity, which satisfies the domain of the test
            val actualCartItem = upsertedItem.find { it.dish.id == cartItem.dish.id }
            assertNotNull(actualCartItem)
            assertEquals(cartItem.quantity, actualCartItem.quantity)
        }

        @Test
        fun remoteFailure_rollbackItem() = runTest {
            // Given remote failure
            val itemToUpdate = cartItems.first()
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            // When an item is upserted into the repository
            repository.upsertCartItem(itemToUpdate.copy(quantity = 100))

            // Then, the getAllCartItems flow emits success with old item
            val upsertedItem = repository.getAllCartItems().first()

            // Testing with direct comparison will require modifying the fakeDao to pass in both the Dish
            // and CartItem so, a simpler approach is to ensure that the item exists, and it has the right
            // quantity, which satisfies the domain of the test
            val actualCartItem = upsertedItem.find { it.dish.id == itemToUpdate.dish.id }
            assertNotNull(actualCartItem)
            assertEquals(itemToUpdate.quantity, actualCartItem.quantity)
        }

        @Test
        fun localFailure_emitErrorMessageStream() = runTest {
            // Given db failure
            localDS = FakeCartDao(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)
            //TODO ()

            // When an item is upserted into the repository
            repository.upsertCartItem(cartItems.first().copy(quantity = 100))

            // Then, the error message channel is updated with an error message
        }

        @Test
        fun remoteFailure_emitErrorMessageStream() = runTest {
            // Given remote failure
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            // When an item is upserted into the repository
            repository.upsertCartItem(cartItems.first().copy(quantity = 100))

            // Then, the error message channel is updated with an error message
            //TODO()
        }
    }


    @Nested
    @OptIn(ExperimentalUuidApi::class)
    inner class GetAllCartItem {

        private val offlineCartItemEntity = List(1) {
            CartItemEntity(Uuid.generateV4().toString(), Random.nextInt(5, 10))
        }
        private val onlineCartDTO = List(1) {
            CartItemDTO(Uuid.generateV4().toString(), Random.nextInt(5, 10))
        }


        @BeforeEach
        fun setUp() {
            remoteDS = FakeCartRemoteDataSource(onlineCartDTO)
            localDS = FakeCartDao(offlineCartItemEntity)

            repository = DefaultCartRepository(remoteDS, localDS)
        }

        @Test
        fun success_returnsOfflineCacheFirst() = runTest {
            val items = repository.getAllCartItems().first()

            val retrievedIds = items.map { it.dish.id }
            offlineCartItemEntity.forEach { (dishId, _) ->
                assertContains(retrievedIds, dishId)
            }
        }

        @Test
        fun success_returnsRemoteDataAfterCacheRefresh() = runTest {
            val items = repository.getAllCartItems().take(2).last()

            val retrievedIds = items.map { it.dish.id }
            val expectedIds =
                offlineCartItemEntity.map { it.dishId } + onlineCartDTO.map { it.dishId }
            expectedIds.forEach { id ->
                assertContains(retrievedIds, id)
            }
        }

        @Test
        fun remoteFailure_returnsCachedData() = runTest {
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            val items = repository.getAllCartItems().first()

            val retrievedIds = items.map { it.dish.id }
            offlineCartItemEntity.forEach { (dishId, _) ->
                assertContains(retrievedIds, dishId)
            }
        }
    }


    @Nested
    inner class ClearCart {

        @Test
        fun success_clearsCart() = runTest {
            // Initial condition: There is some data in the cache
            val beforeClearing = repository.getAllCartItems().first()
            assertNotEquals(0, beforeClearing.size)

            repository.clearCart()
            // Exit condition: No data is in the cache
            val afterClearing = repository.getAllCartItems().first()
            assertEquals(0, afterClearing.size)
        }

        @Test
        fun success_returnsSuccess() = runTest {
            assertIs<Resource.Success<Unit>>(repository.clearCart())
        }

        @Test
        fun remoteFailure_doesNotClearCart() = runTest {
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            repository.clearCart()

            val afterClearing = repository.getAllCartItems().first()
            assertNotEquals(0, afterClearing.size)
        }

        @Test
        fun remoteFailure_returnsFailure() = runTest {
            remoteDS = FakeCartRemoteDataSource(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            assertIs<Resource.Failure<Unit>>(repository.clearCart())
        }

        @Test
        fun dbFailure_returnsFailure() = runTest {
            localDS = FakeCartDao(throwError = true)
            repository = DefaultCartRepository(remoteDS, localDS)

            assertIs<Resource.Failure<Unit>>(repository.clearCart())
        }
    }

}