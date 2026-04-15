package com.littlelemon.application.menu.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.littlelemon.application.R
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.presentation.MenuState
import com.littlelemon.application.menu.utils.DishGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MenuScreenRootTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()


    private val dishes = List(10) { DishGenerator.generateDish() }
    private val categories = dishes.fold(listOf<Category>()) { list, dish -> list + dish.category }
    private val state = MenuState(dishes = dishes)
    // TODO: Add error state test

    @Test
    fun displaysHeader() {
        composeTestRule.setContent {
            MenuScreenRoot(state)
        }
        composeTestRule.onNodeWithText(application.getString(R.string.heading_explore_our_cuisines))
            .assertIsDisplayed()
    }

    @Test
    fun displaysFilter() {
        composeTestRule.setContent {
            MenuScreenRoot(state)
        }
        composeTestRule.onNodeWithText(application.getString(R.string.act_filter))
            .assertIsDisplayed()
    }

    @Test
    fun displaysCategories() {
        composeTestRule.setContent {
            MenuScreenRoot(state)
        }
        categories.forEach { category ->
            composeTestRule.onNodeWithText(category.categoryName).performScrollTo()
                .assertIsDisplayed()
        }
    }

    // TODO: Fix test
    @Test
    fun displaysDish() {
        composeTestRule.setContent {
            MenuScreenRoot(state)
        }
        // Since this is an instrumentation test for menu card, checking that the title is
        // displayed implicitly verifies that the card itself is displayed.
        // Also since the text is single line and ellipsis terminated when overflown,
        // scrolling and testing for displayed title will fail, so `assertExists` to ensure that the card exists.
        dishes.forEachIndexed { index, dish ->
            composeTestRule.onNodeWithText(dish.title, substring = true).performScrollToIndex(index)
                .assertExists()
        }
    }

}