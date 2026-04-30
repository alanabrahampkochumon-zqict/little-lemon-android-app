package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.shared.cart.domain.models.CartItem
import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.shared.menu.data.mappers.toDish


fun List<CartItemDetails>.toCartItems(): List<CartItem> {
    return map { (cartItem, dishEntity) ->
        CartItem(dishEntity.toDish(), cartItem.quantity)
    }
}