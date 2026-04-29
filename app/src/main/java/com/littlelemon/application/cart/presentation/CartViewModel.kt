package com.littlelemon.application.cart.presentation

import androidx.lifecycle.ViewModel
import com.littlelemon.application.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.cart.domain.usecase.UpsertCartItemUseCase
import kotlinx.coroutines.flow.SharedFlow

class CartViewModel(
    private val getCartItems: GetCartItemsUseCase,
    private val getCartError: GetCartErrorMessagesUseCase,
    private val upsertCartItem: UpsertCartItemUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {

    val errorState: SharedFlow<String> = getCartError()
}