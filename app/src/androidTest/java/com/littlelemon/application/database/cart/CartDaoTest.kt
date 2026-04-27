package com.littlelemon.application.database.cart

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.littlelemon.application.database.LittleLemonDatabase
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.MenuDao
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CartDaoTest {


    private lateinit var cartDao: CartDao
    private lateinit var menuDao: MenuDao
    private lateinit var testDatabase: LittleLemonDatabase

    private val dishes = List(5) { DishGenerator.generateDishEntity().first }

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        testDatabase =
            Room.inMemoryDatabaseBuilder(context, LittleLemonDatabase::class.java).build()
        cartDao = testDatabase.cartDao
        runBlocking {
            menuDao.insertDishes(dishes)
        }
    }

    @After
    fun tearDown() {
        testDatabase.close()
    }


    @Test
    fun upsertCartItem_addsNewCartItem() = runTest {
        // Given an empty database
        val cartItem = CartItemEntity(dishes[0].dishId, 5)

        // When cart item is upserted
        cartDao.upsertCartItem(cartItem)

        // Then it is inserted
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

        // Then the cart item is updated.
        assertEquals(cartItem, cartDetails.first().cartItem)
    }

}