package com.littlelemon.application.cart.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.cart.presentation.CartState
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.random.Random
import kotlin.test.assertTrue


@RunWith(RobolectricTestRunner::class)
class CartScreenContentTest {


    val cartDetailItems = List(5) {
        DishGenerator.generateDish()
    }.map { CartDetailItem(it, Random.nextInt(3, 5)) }


    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()


    @Test
    @Config(qualifiers = "h1280dp")
    fun cartItemSection_displaysAllCartItem() = runTest {
        testRule.setContent {
            LazyColumn {
                cartItemsSection(CartState(cartDetailItems = cartDetailItems), {}, {}, {})
            }
        }

        cartDetailItems.forEach { cartDetailItem ->
            testRule.onNodeWithText(cartDetailItem.dish.title).assertIsDisplayed()
        }
    }

    @Test
    fun cartItemSection_onIncreaseItem_triggersCallback() = runTest {
        var callbackTriggered = false
        testRule.setContent {
            LazyColumn {
                cartItemsSection(
                    CartState(cartDetailItems = cartDetailItems),
                    { callbackTriggered = true },
                    {},
                    {})
            }
        }

        testRule.onAllNodesWithTag(CoreTestTags.STEPPER_INCREASE).onFirst().performClick()

        assertTrue { callbackTriggered }
    }

    @Test
    fun cartItemSection_onDecreaseItem_triggersCallback() = runTest {
        var callbackTriggered = false
        testRule.setContent {
            LazyColumn {
                cartItemsSection(
                    CartState(cartDetailItems = cartDetailItems),
                    { },
                    { callbackTriggered = true },
                    {})
            }
        }

        testRule.onAllNodesWithTag(CoreTestTags.STEPPER_DECREASE).onFirst().performClick()

        assertTrue { callbackTriggered }
    }

    @Test
    fun cartItemSection_onRemoveItem_triggersCallback() = runTest {
        var callbackTriggered = false
        testRule.setContent {
            LazyColumn {
                cartItemsSection(
                    CartState(cartDetailItems = cartDetailItems),
                    { },
                    { },
                    { callbackTriggered = true })
            }
        }

        testRule.onAllNodesWithText(application.getString(R.string.act_remove)).onFirst()
            .performClick()

        assertTrue { callbackTriggered }
    }


    @Test
    fun pricingSection_displaysSubtotal() {
        val subtotal = 55.83
        val discountedPrice = 49.59
        testRule.setContent {
            PricingSection(subtotal, discountedPrice, 0)
        }
        testRule.onNodeWithText(subtotal.toString(), substring = true).assertIsDisplayed()
    }

    @Test
    fun pricingSection_displaysDiscountedPrice() {
        val subtotal = 55.83
        val discountedPrice = 49.59
        testRule.setContent {
            PricingSection(subtotal, discountedPrice, 0)
        }
        testRule.onNodeWithText(discountedPrice.toString(), substring = true).assertIsDisplayed()
    }

    @Test
    fun pricingSection_displaysDiscount() {
        val subtotal = 55.83
        val discountedPrice = 49.59
        testRule.setContent {
            PricingSection(subtotal, discountedPrice, 0)
        }
        testRule.onNodeWithText(
            application.getString(
                R.string.price_format,
                (subtotal - discountedPrice)
            ), substring = true
        )
            .assertIsDisplayed()
    }


}