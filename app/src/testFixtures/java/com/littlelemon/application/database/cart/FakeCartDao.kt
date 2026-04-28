package com.littlelemon.application.database.cart

import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import com.littlelemon.application.menu.utils.DishGenerator
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi

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
    val dishes = mutableListOf<DishWithCategories>()

    init {
        if (initialItems == null) {
            val seedCount = Random.nextInt(5, 10)
            repeat(seedCount) {
                val dish = DishGenerator.generateDishWithCategories(1, 1).first().first
                dishes.add(dish)
                database.add(CartItemEntity(dish.dish.dishId, Random.nextInt(5, 10)))
            }
        } else {
            database.addAll(initialItems)
            dishes.addAll(initialItems.map { cartItemEntity ->
                val dish = DishGenerator.generateDishWithCategories(1, 1).first().first
                dish.copy(dish = dish.dish.copy(dishId = cartItemEntity.dishId))
            })
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
                it,
                dishes.find { (dish) -> dish.dishId == it.dishId }
                    ?: DishGenerator.generateDishWithCategories(1, 1).first().first
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