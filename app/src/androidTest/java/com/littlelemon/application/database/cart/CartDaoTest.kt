@file:OptIn(ExperimentalUuidApi::class)

package com.littlelemon.application.database.cart

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.littlelemon.application.database.LittleLemonDatabase
import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.MenuDao
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CartDaoTest {


    private lateinit var cartDao: CartDao
    private lateinit var menuDao: MenuDao
    private lateinit var database: LittleLemonDatabase

    private val dishes = List(5) { DishGenerator.generateDishEntity().first }

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        database =
            Room.inMemoryDatabaseBuilder(context, LittleLemonDatabase::class.java).build()
        cartDao = database.cartDao
        menuDao = database.menuDao
        runBlocking {
            menuDao.insertDishes(dishes)
        }
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun upsertCartItem_addsNewCartItem() = runTest {
        // Given an empty database
        val cartItem = CartItemEntity(dishes[0].dishId, 5)

        // When cart item is upserted
        cartDao.upsertCartItem(cartItem)

        // Then, it is inserted
        val cartDetails = cartDao.getAllCartItems().first()
        assertEquals(cartItem, cartDetails.first().cartItem)
    }

    @Test
    fun upsertCartItem_updatesExistingCartItem() = runTest {
        // Given a `CartItem` already exists in the database
        val cartItem = CartItemEntity(dishes[0].dishId, 2)
        cartDao.upsertCartItem(cartItem.copy(quantity = 5))

        // When quantity is updated
        cartDao.upsertCartItem(cartItem)

        // And queried
        val cartDetails = cartDao.getAllCartItems().first()

        // Then, the cart item is updated.
        assertEquals(cartItem, cartDetails.first().cartItem)
    }


    @Test
    fun removeCartItem_validId_removesItem() = runTest {
        // Given a non-empty database
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val removeId = ids[0]
        dishes.forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is removed
        cartDao.removeCartItem(removeId)

        // Then, it is removed
        val cartDetails = cartDao.getAllCartItems().first()
        assertTrue { cartDetails.indexOfFirst { it.cartItem.id == removeId } == -1 }
    }

    @Test
    fun removeCartItem_invalidId_removesNoItem() = runTest {
        // Given a non-empty database
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val removeId = Uuid.generateV4().toString()
        dishes.forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is removed with invalid ID is removed
        cartDao.removeCartItem(removeId)

        // Then, no item is removed
        val cartDetails = cartDao.getAllCartItems().first()
        assertTrue {
            dishes.all { dish ->
                cartDetails.map { cartDetailItem -> cartDetailItem.dish.dishId }
                    .contains(dish.dishId)
            }
        }
    }


    @Test
    fun upsertOrRemoveCartItem_nonExistentItem_addsIt() = runTest {
        // Given a non-empty database
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val newCartItem = CartItemEntity(dishes[0].dishId, 4, Uuid.generateV4().toString())
        dishes.drop(1).forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is upserted
        cartDao.upsertOrRemoveCartItem(newCartItem)

        // Then, it is inserted
        val cartDetails = cartDao.getAllCartItems().first()
        assertTrue { cartDetails.map { it.cartItem.id }.contains(newCartItem.id) }
    }


    @Test
    fun upsertOrRemoveCartItem_existentItem_updatesItem() = runTest {
        // Given a non-empty database with the cart item in it
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val updatedCartItem = CartItemEntity(dishes[0].dishId, 100, ids[0])
        dishes.forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is upserted
        cartDao.upsertOrRemoveCartItem(updatedCartItem)

        // Then, it is updated
        val cartDetails = cartDao.getAllCartItems().first()
        assertEquals(
            updatedCartItem.quantity,
            cartDetails.find { it.cartItem.id == updatedCartItem.id }?.cartItem?.quantity ?: 0
        )
    }


    @Test
    fun upsertOrRemoveCartItem_zeroQuantity_removesItem() = runTest {
        // Given a non-empty database with the cart item in it
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val updatedCartItem = CartItemEntity(dishes[0].dishId, 0, ids[0])
        dishes.forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is upserted
        cartDao.upsertOrRemoveCartItem(updatedCartItem)

        // Then, it is updated
        val cartDetails = cartDao.getAllCartItems().first()
        assertTrue { cartDetails.indexOfFirst { it.cartItem.id == updatedCartItem.id } == -1 }
    }

    @Test
    fun upsertOrRemoveCartItem_negativeQuantity_removesItem() = runTest {
        // Given a non-empty database with the cart item in it
        val ids = List(dishes.size) { Uuid.generateV4().toString() }
        val updatedCartItem = CartItemEntity(dishes[0].dishId, 0, ids[0])
        dishes.forEachIndexed { index, dish ->
            cartDao.upsertCartItem(CartItemEntity(dish.dishId, Random.nextInt(3, 5), ids[index]))
        }

        // When cart item is upserted
        cartDao.upsertOrRemoveCartItem(updatedCartItem)

        // Then, it is updated
        val cartDetails = cartDao.getAllCartItems().first()
        assertTrue { cartDetails.indexOfFirst { it.cartItem.id == updatedCartItem.id } == -1 }
    }

    @Test
    fun getAllCartItems_emptyDb_returnsEmptyList() = runTest {
        val result = cartDao.getAllCartItems().first()
        assertEquals(0, result.size)
    }

    @Test
    fun getAllCartItems_nonEmptyDb_returnsListOfCartDetails() = runTest {
        // Given a non-empty database
        val cartItems = dishes.map { CartItemEntity(it.dishId, Random.nextInt(3, 5)) }
        val cartDetails = cartItems.mapIndexed { index, cartItem ->
            CartItemDetails(
                cartItem,
                dish = dishes[index]
            )
        }
        cartItems.forEach { cartItem ->
            cartDao.upsertCartItem(cartItem)
        }

        // When queried for cartItems
        val queriedCartItems = cartDao.getAllCartItems().first()

        // Then it contains all the items
        assertTrue { cartDetails.all { cartDetail -> queriedCartItems.contains(cartDetail) } }
    }

}