package com.littlelemon.application.home.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.home.HomeTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CategoryCardTest {


    @get:Rule
    val testRule = createComposeRule()

    private val label = "category card"

    @Test
    fun displaysLabel() {
        // Given a category card
        testRule.setContent {
            CategoryCard(label, false, {})
        }

        // Then, label is displayed
        testRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun whenSelected_displaysIcon() {
        // Given a category card with selected status
        testRule.setContent {
            CategoryCard(label, true, {})
        }

        // Then, check icon is displayed
        testRule.onNodeWithTag(HomeTestTags.CATEGORY_CARD_CHECK_MARK, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun whenSelected_getSelectedSemantically() {
        // Given a category card with selected status
        val tag = "CatCard"
        testRule.setContent {
            CategoryCard(label, true, {}, modifier = Modifier.testTag(tag))
        }

        // Then, card is selected
        testRule.onNodeWithTag(tag).assertIsSelected()
    }

    @Test
    fun whenNotSelected_doesNotDisplaysIcon() {
        // Given a category card with unselected status
        testRule.setContent {
            CategoryCard(label, false, {})
        }

        // Then, check icon is not displayed
        testRule.onNodeWithTag(HomeTestTags.CATEGORY_CARD_CHECK_MARK, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun whenNotSelected_isNotSemanticallySelected() {
        // Given a category card with unselected status
        val tag = "CatCard"
        testRule.setContent {
            CategoryCard(label, false, {}, modifier = Modifier.testTag(tag))
        }

        // Then, card is not selected
        testRule.onNodeWithTag(tag).assertIsNotSelected()
    }

    @Test
    fun onClick_triggersCallback() {
        // Given a category card
        var callbackTriggered = false
        testRule.setContent {
            CategoryCard(label, true, { callbackTriggered = true })
        }

        // When card is selected
        testRule.onNodeWithTag(HomeTestTags.CATEGORY_CARD_CHECK_MARK, useUnmergedTree = true)
            .performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun whenDisabled_disableInteraction() {
        // Given a disabled category card
        val tag = "CatCard"
        testRule.setContent {
            CategoryCard(label, false, {}, modifier = Modifier.testTag(tag), enabled = false)
        }

        // Then, card is disabled
        testRule.onNodeWithTag(tag).assertIsNotEnabled()
    }
}