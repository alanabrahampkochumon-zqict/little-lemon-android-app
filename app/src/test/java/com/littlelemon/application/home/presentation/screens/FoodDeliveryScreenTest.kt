package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.home.presentation.HomeState
import com.littlelemon.application.menu.utils.DishGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class FoodDeliveryScreenTest {

    @get:Rule
    val testRule = createComposeRule()

    val dishes = List(2) { DishGenerator.generateDish() }
    val categories = dishes.flatMap { dish -> dish.category }.distinct()

    private val application = RuntimeEnvironment.getApplication()


    @Test
    fun categoryHeaderIsDisplayed() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(HomeState(), {}, {}, 12.dp)
            }
        }

        testRule.onNodeWithText(application.getString(R.string.heading_order_for_delivery))
            .assertIsDisplayed()
    }

    @Test
    fun popularOrderHeaderIsDisplayed() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(HomeState(), {}, {}, 12.dp)
            }
        }

        testRule.onNodeWithText(application.getString(R.string.heading_popular_orders))
            .assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "h1000dp")
    fun displaysDishes() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(
                    HomeState(popularDishes = dishes, categories = categories),
                    {},
                    {},
                    12.dp
                )
            }
        }
        dishes.forEach { dish ->
            testRule.onNodeWithText(dish.title)
                .assertIsDisplayed()
        }
    }


    @Test
    fun displaysCategories() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(
                    HomeState(popularDishes = dishes, categories = categories),
                    {},
                    {},
                    12.dp
                )
            }
        }
        categories.forEach { category ->
            testRule.onNodeWithText(category.categoryName)
                .assertIsDisplayed()
        }
    }

    @Test
    fun onCategoryChange_triggersCallback() {
        var categoryTriggered = ""
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(
                    HomeState(popularDishes = dishes, categories = categories),
                    { categoryTriggered = it },
                    {},
                    12.dp
                )
            }
        }
        categories.forEach { category ->
            testRule.onNodeWithText(category.categoryName)
                .performClick()
            assertEquals(category.categoryName, categoryTriggered)
        }
    }


    @Test
    fun onViewAll_triggersCallback() {
        var callbackTriggered = false
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(
                    HomeState(popularDishes = dishes, categories = categories),
                    {},
                    { callbackTriggered = true },
                    12.dp
                )
            }
        }
        testRule.onNodeWithText(application.getString(R.string.view_all)).performClick()
        assertTrue(callbackTriggered)
    }
}