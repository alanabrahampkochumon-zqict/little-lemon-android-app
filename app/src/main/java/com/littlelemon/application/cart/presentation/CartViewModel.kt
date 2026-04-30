package com.littlelemon.application.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.cart.domain.usecase.UpsertCartItemUseCase
import com.littlelemon.application.core.presentation.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    getCartItems: GetCartItemsUseCase,
    getCartError: GetCartErrorMessagesUseCase,
    private val upsertCartItem: UpsertCartItemUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {

    val state = combine(getCartError(), getCartItems()) { errorMessages, cartItems ->
        CartState(
            isLoading = false,
            errorMessage = UiText.DynamicString(errorMessages),
            cartItems = cartItems
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CartState(isLoading = true, null, emptyList())
    )


    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.DecreaseQuantity -> viewModelScope.launch {
                upsertCartItem(action.cartItem.copy(quantity = action.cartItem.quantity - 1))
            }

            is CartAction.IncreaseQuantity -> viewModelScope.launch {
                upsertCartItem(action.cartItem.copy(quantity = action.cartItem.quantity + 1))
            }

            is CartAction.RemoveItem -> TODO()
        }
    }
}