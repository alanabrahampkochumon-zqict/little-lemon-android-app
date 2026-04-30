package com.littlelemon.application.cart.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.cart.presentation.screen.components.CartItemCard
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CartItemCardTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()
    val priceFormat = R.string.price_format

    private val cartItem = CartItem(
        dish = Dish(
            id = "",
            title = "Dish Name",
            description = "Dish Description",
            price = 100.00,
            imageURL = "testurl",
            stock = 100,
            nutritionInfo = NutritionInfo(100, 1, 1, 1),
            discountedPrice = 50.00,
            category = listOf(),
            dateAdded = LocalDateTime(2026, 12, 12, 12, 12, 12),
            popularityIndex = 100
        ), 5
    )

    @Test
    fun dishTitle_isDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            CartItemCard(cartItem, {}, {}, {})
        }

        // Then, title is displayed
        testRule.onNodeWithText(cartItem.dish.title).assertIsDisplayed()
    }

    @Test
    fun dishPrice_isDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            CartItemCard(cartItem, {}, {}, {})
        }

        // Then, price per item is displayed
        testRule.onNodeWithText(
            application.getString(
                R.string.price_format_ea, cartItem.dish.price
            )
        ).assertIsDisplayed()
    }

    @Test
    fun discountedTotalPrice_isDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            CartItemCard(cartItem, {}, {}, {})
        }

        // Then, total discounted price is displayed
        testRule.onNodeWithText(
            application.getString(
                priceFormat, cartItem.dish.discountedPrice?.times(cartItem.quantity)
            )
        ).assertIsDisplayed()
    }

    @Test
    fun totalPrice_isDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            CartItemCard(cartItem, {}, {}, {})
        }

        // Then, total price is displayed
        testRule.onNodeWithText(
            "$" + application.getString(
                priceFormat, (cartItem.dish.price * cartItem.quantity)
            )
        ).assertIsDisplayed()
    }

    @Test
    fun noDiscountPrice_normalPriceIsDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            CartItemCard(
                cartItem.copy(dish = cartItem.dish.copy(discountedPrice = null)),
                {},
                {},
                {})
        }

        // Then, total price is displayed
        // NOTE: In the main price area, the $ is separate from the actual amount.
        testRule.onNodeWithText(
            application.getString(
                priceFormat, (cartItem.dish.price * cartItem.quantity)
            )
        ).assertIsDisplayed()
    }

    @Test
    fun onItemIncrement_triggersCallback() {
        // Given an order item
        var callbackTriggered = false
        testRule.setContent {
            CartItemCard(
                cartItem.copy(dish = cartItem.dish.copy(discountedPrice = null)),
                { callbackTriggered = true },
                {},
                {})
        }

        // When the increase button is clicked
        testRule.onNodeWithTag(CoreTestTags.STEPPER_INCREASE).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun onItemDecrement_triggersCallback() {
        // Given an order item
        var callbackTriggered = false
        testRule.setContent {
            CartItemCard(
                cartItem.copy(dish = cartItem.dish.copy(discountedPrice = null)),
                { },
                { callbackTriggered = true },
                {})
        }

        // When the decrease quantity button is clicked
        testRule.onNodeWithTag(CoreTestTags.STEPPER_DECREASE).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun onRemoveItem_triggersCallback() {
        // Given an order item
        var callbackTriggered = false
        testRule.setContent {
            CartItemCard(
                cartItem.copy(dish = cartItem.dish.copy(discountedPrice = null)),
                { },
                { },
                { callbackTriggered = true })
        }

        // When the remove button is clicked
        testRule.onNodeWithText(application.getString(R.string.act_remove)).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

}