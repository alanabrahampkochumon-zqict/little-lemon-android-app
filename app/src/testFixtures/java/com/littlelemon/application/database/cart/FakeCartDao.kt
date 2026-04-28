package com.littlelemon.application.database.cart

import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// NOTE: Keep initial items null to seed the database
//       and pass in a emptyList to seed with no entries.
@OptIn(ExperimentalUuidApi::class)
class FakeCartDao(
    initialItems: List<CartItemEntity>? = null,
    private val throwError: Boolean = false
) :
    CartDao {

    private val faker = faker {}

    private var database = mutableListOf<CartItemEntity>()

    init {
        if (initialItems == null) {
            val seedCount = Random.nextInt(5, 10)
            repeat(seedCount) {
                database.add(CartItemEntity(Uuid.generateV4().toString(), Random.nextInt(5, 10)))
            }
        } else {
            database.addAll(initialItems)
        }
    }


    override suspend fun upsertCartItem(cartItem: CartItemEntity) {
        if (throwError)
            throw IllegalArgumentException()
        if (cartItem in database)
            database.remove(cartItem)
        database.add(cartItem)
    }

    override suspend fun removeCartItem(dishId: String) {
        if (throwError)
            throw IllegalArgumentException()
        database = database.filter { it.dishId != dishId }.toMutableList()
    }

    override fun getAllCartItems(): Flow<List<CartItemDetails>> = flow {
        if (throwError)
            throw IllegalArgumentException()
        emit(database.map {
            CartItemDetails(
                it, DishWithCategories(
                    DishEntity(
                        dishId = Uuid.generateV4().toString(),
                        title = faker.dessert.dessert()(),
                        description = faker.lorem.words(),
                        price = Random.nextDouble(10.0, 15.0),
                        image = "",
                        stock = Random.nextInt(20, 50),
                        nutritionInfo = null,
                        discountedPrice = 0.0,
                        popularityIndex = Random.nextInt(20, 50),
                        dateAdded = "2024-05-24T11:42:36Z"
                    ), categories = emptyList()
                )
            )
        })
    }

    override fun clearCartItems() {
        if (throwError)
            throw IllegalArgumentException()
        database.clear()
    }

    override suspend fun getQuantity(dishId: String): Int {
        return database.find { it.dishId == dishId }?.quantity ?: 0
    }

}