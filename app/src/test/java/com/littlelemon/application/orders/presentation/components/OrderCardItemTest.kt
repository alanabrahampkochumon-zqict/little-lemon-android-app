package com.littlelemon.application.orders.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.orders.domain.models.MenuItem
import com.littlelemon.application.orders.domain.models.OrderItem
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OrderCardItemTest {

    @get:Rule
    val testRule = createComposeRule()

    val orderItem = OrderItem(
        orderName = "Greek Salad & Bruschetta",
        orderStatus = OrderItem.OrderStatus.Delivered,
        menuItems = listOf(
            MenuItem(
                Dish(
                    title = "Dish 1",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 5
            ),
            MenuItem(
                Dish(
                    title = "Dish 2",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 10
            ), MenuItem(
                Dish(
                    title = "Dish 3",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 1
            ), MenuItem(
                Dish(
                    title = "Dish 4",
                    description = "Long Description",
                    price = 2.2,
                    imageURL = "",
                    stock = 12,
                    nutritionInfo = null,
                    discountedPrice = 2.2,
                    category = listOf<Category>(),
                    dateAdded = LocalDateTime(2025, 12, 20, 12, 30),
                    popularityIndex = 123
                ), quantity = 1
            )
        ),
        orderDate = LocalDateTime(2025, 12, 30, 12, 30),
        specialInstructions = "Avoid using coconut milk",
        paymentMode = "Card ending in 3521",
        deliveryAddressLabel = "Work Address",
        billAmount = 55.45,
        deliveryCharge = 0.0,
        totalAmount = 55.45,
        refundDate = null
    )

    val orderDate = "December 30, 2025"

    @Test
    fun displaysTitle() {
        testRule.setContent {
            OrderCardItem(orderItem, {})
        }
        testRule.onNodeWithText(orderItem.orderName).assertIsDisplayed()
    }

    @Test
    fun displaysOrderDate() {
        testRule.setContent {
            OrderCardItem(orderItem, {})
        }
        testRule.onNodeWithText(orderDate).assertIsDisplayed()
    }

    @Test
    fun expandedCard_displaysOrderStatus() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(orderItem.orderStatus.name)
            .assertIsDisplayed()
    }
    
    @Test
    fun expandedCard_displaysSpecialInstructions() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(orderItem.specialInstructions!!).assertIsDisplayed()
    }

}