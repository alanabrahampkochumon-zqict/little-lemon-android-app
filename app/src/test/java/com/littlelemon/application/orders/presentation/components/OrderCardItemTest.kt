package com.littlelemon.application.orders.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.orders.domain.models.MenuItem
import com.littlelemon.application.orders.domain.models.OrderItem
import com.littlelemon.application.shared.menu.domain.models.Category
import com.littlelemon.application.shared.menu.domain.models.Dish
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1000dp-h1000dp-480dpi")
class OrderCardItemTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()
    val orderItem = OrderItem(
        orderName = "Greek Salad & Bruschetta",
        orderStatus = OrderItem.OrderStatus.Delivered,
        menuItems = listOf(
            MenuItem(
                Dish(
                    id = "",
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
                    id = "",
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
                    id = "",
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
                    id = "",
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
        deliveryCharge = 1.25,
        totalAmount = 56.70,
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

    @Test
    fun expandedCard_displaysPaymentMode() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(orderItem.paymentMode).assertIsDisplayed()
    }

    @Test
    fun expandedCard_displaysBillAmount() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(
            application.getString(R.string.price_format, orderItem.billAmount),
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun expandedCard_displaysDeliveryCharge() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(
            application.getString(R.string.price_format, orderItem.deliveryCharge),
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun expandedCard_displaysTotalAmount() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = true)
        }
        testRule.onNodeWithText(
            application.getString(
                R.string.price_format,
                orderItem.totalAmount
            ),
            substring = true
        ).assertIsDisplayed()
    }


    @Test
    fun nonExpandedCard_doesNotDisplayOrderStatus() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = false)
        }
        testRule.onNodeWithText(orderItem.orderStatus.name)
            .assertIsNotDisplayed()
    }

    @Test
    fun nonExpandedCard_doesNotDisplaySpecialInstructions() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = false)
        }
        testRule.onNodeWithText(orderItem.specialInstructions!!).assertIsNotDisplayed()
    }

    @Test
    fun nonExpandedCard_doesNotDisplayPaymentMode() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = false)
        }
        testRule.onNodeWithText(orderItem.paymentMode).assertIsNotDisplayed()
    }

    @Test
    fun nonExpandedCard_doesNotDisplayBillAmount() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = false)
        }
        testRule.onNodeWithText(
            application.getString(R.string.price_format, orderItem.billAmount),
            substring = true
        ).assertIsNotDisplayed()
    }

    @Test
    fun nonExpandedCard_doesNotDisplayDeliveryCharge() {
        testRule.setContent {
            OrderCardItem(orderItem, {}, expanded = false)
        }
        testRule.onNodeWithText(
            application.getString(R.string.price_format, orderItem.deliveryCharge),
            substring = true
        ).assertIsNotDisplayed()
    }

    val cancelledOrder = orderItem.copy(
        orderStatus = OrderItem.OrderStatus.Cancelled,
        refundDate = LocalDateTime(2025, 12, 29, 13, 30)
    )
    val cancelledDate = "December 29, 2025"

    @Test
    fun cancelledOrder_displaysCancelledDate() {
        testRule.setContent {
            OrderCardItem(cancelledOrder, {}, expanded = true)
        }
        testRule.onNodeWithText(
            cancelledDate,
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun onReorderClick_triggersCallback() {
        // Given an order card item
        var callbackTriggered = false
        testRule.setContent {
            OrderCardItem(orderItem, { callbackTriggered = true }, expanded = false)
        }

        // When reorder button is clicked
        testRule.onNodeWithText(application.getString(R.string.reorder)).performClick()

        // Then it triggers a callback
        assertTrue { callbackTriggered }
    }

}