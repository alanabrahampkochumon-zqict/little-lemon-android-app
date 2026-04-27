package com.littlelemon.application.database.cart

import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.models.DishEntity
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// NOTE: Keep initial items null to seed the database
//       and pass in a emptyList to seed with no entries.
@OptIn(ExperimentalUuidApi::class)
class FakeCartDao(initialItems: List<CartItemEntity>? = null, throwError: Boolean = false) :
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
        if (cartItem in database)
            database.remove(cartItem)
        database.add(cartItem)
    }

    override suspend fun removeCartItem(id: String) {
        database = database.filter { it.id != id }.toMutableList()
    }

    override fun getAllCartItems(): Flow<List<CartItemDetails>> = flow {
        emit(database.map {
            CartItemDetails(
                it, DishEntity(
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
                )
            )
        })
    }

    override fun clearCartItems() {
        database.clear()
    }

}