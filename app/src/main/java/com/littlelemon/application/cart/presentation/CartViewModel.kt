package com.littlelemon.application.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.shared.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import com.littlelemon.application.shared.cart.domain.usecase.RefreshCartUseCase
import com.littlelemon.application.shared.cart.domain.usecase.UpsertCartItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CartViewModel(
    getCartItems: GetCartItemDetailsUseCase,
    getCartError: GetCartErrorMessagesUseCase,
    refreshCart: RefreshCartUseCase,
    private val upsertCartItem: UpsertCartItemUseCase,
    private val clearCart: ClearCartUseCase,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    val state = combine(getCartError(), getCartItems()) { errorMessages, cartItems ->
        CartState(
            isLoading = false,
            errorMessage = UiText.DynamicString(errorMessages),
            cartDetailItems = cartItems
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CartState(isLoading = true, null, emptyList())
    )

    init {
        viewModelScope.launch(coroutineContext) {
            refreshCart()
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.DecreaseQuantity -> viewModelScope.launch {
                upsertCartItem(action.cartDetailItem.copy(quantity = action.cartDetailItem.quantity - 1))
            }

            is CartAction.IncreaseQuantity -> viewModelScope.launch {
                upsertCartItem(action.cartDetailItem.copy(quantity = action.cartDetailItem.quantity + 1))
            }

            is CartAction.RemoveItem -> viewModelScope.launch {
                upsertCartItem(action.cartDetailItem.copy(quantity = 0))
            }
        }
    }
}