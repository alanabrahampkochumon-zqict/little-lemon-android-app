package com.littlelemon.application.menu.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.menu.MenuTestTags
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.presentation.MenuState
import com.littlelemon.application.menu.utils.DishGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals

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
            MenuScreenRoot(state, null, {}, {}, {})
        }
        composeTestRule.onNodeWithText(application.getString(R.string.heading_explore_our_cuisines))
            .assertIsDisplayed()
    }

    @Test
    fun displaysFilter() {
        composeTestRule.setContent {
            MenuScreenRoot(state, null, {}, {}, {})
        }
        composeTestRule.onNodeWithText(application.getString(R.string.act_filter))
            .assertIsDisplayed()
    }

    @Test
    fun displaysCategories() {
        composeTestRule.setContent {
            MenuScreenRoot(state, null, {}, {}, {})
        }
        categories.forEach { category ->
            composeTestRule.onNodeWithText(category.categoryName).performScrollTo()
                .assertIsDisplayed()
        }
    }


    @Test
    fun displaysDish() {
        composeTestRule.setContent {
            MenuScreenRoot(state, null, {}, {}, {})
        }
        // Since this is an instrumentation test for menu card, checking that the title is
        // displayed implicitly verifies that the card itself is displayed.
        dishes.forEach { dish ->
            composeTestRule.onNodeWithTag(MenuTestTags.MENU_ITEM_LIST)
                .performScrollToNode(hasText(dish.title))
                .assertIsDisplayed()
        }
    }


    @Test
    fun onIncreaseQuantity_triggersCallbackWithDish() {
        val dishCardIndex = 0
        var triggeredDish: Dish? = null
        composeTestRule.setContent {
            MenuScreenRoot(state, null, {}, { triggeredDish = it }, {})
        }

        composeTestRule.onAllNodesWithTag(CoreTestTags.STEPPER_INCREASE)[dishCardIndex].performClick()

        assertEquals(dishes[dishCardIndex], triggeredDish)
    }


    @Test
    fun onDecreaseQuantity_triggersCallbackWithDish() {
        val dishCardIndex = 0
        var triggeredDish: Dish? = null
        composeTestRule.setContent {
            MenuScreenRoot(state, null, {}, { }, { triggeredDish = it })
        }

        composeTestRule.onAllNodesWithTag(CoreTestTags.STEPPER_DECREASE)[dishCardIndex].performClick()

        assertEquals(dishes[dishCardIndex], triggeredDish)
    }

    @Test
    fun onCategoryChange_triggersCallbackWithCategory() {
        val categoryIndex = 0
        var categoryTriggered: String? = ""
        composeTestRule.setContent {
            MenuScreenRoot(state, null, { categoryTriggered = it }, { }, {})
        }

        composeTestRule.onNodeWithText(categories[categoryIndex].categoryName).performClick()

        assertEquals(categories[categoryIndex].categoryName, categoryTriggered)
    }

}