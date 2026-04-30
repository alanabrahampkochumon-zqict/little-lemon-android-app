package com.littlelemon.application.cart.data

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakeCartRepository(
    initialItems: List<CartDetailItem>? = null,
    private val throwError: Boolean = false
) :
    CartRepository {

    companion object {
        const val ERROR_MESSAGE = "There was an error with the repository"
    }

    private val _errorMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val errorMessages: SharedFlow<String>
        get() = _errorMessages

    private val _data = mutableListOf<CartDetailItem>()

    init {
        if (initialItems == null) {
            val range = Random.nextInt(5, 10)
            repeat(range) {
                val dish = DishGenerator.generateDish()
                val cartDetailItem = CartDetailItem(dish = dish, quantity = Random.nextInt(3, 5))
                _data.add(cartDetailItem)
            }
        } else {
            _data.addAll(initialItems)
        }
    }

    override suspend fun upsertCartItem(cartDetailItem: CartDetailItem) {
        if (throwError)
            _errorMessages.tryEmit(ERROR_MESSAGE)
        _data.add(cartDetailItem)
    }

    override suspend fun clearCart(): Resource<Unit> {
        if (throwError)
            return Resource.Failure(errorMessage = ERROR_MESSAGE)
        _data.clear()
        return Resource.Success()
    }

    override fun getAllCartItems(): Flow<List<CartDetailItem>> = flow {
        if (throwError) {
            _errorMessages.tryEmit(ERROR_MESSAGE)
            emit(emptyList())
        } else {
            emit(_data)
        }
    }
}