package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.core.CoreTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class FilterListItemTest {


    @get:Rule
    val testRule = createComposeRule()

    private val label = "Label"
    private val testTag = "FilterItemTestTag"

    @Test
    fun displaysLabel() {
        // Given a filter list item
        testRule.setContent {
            FilterListItem(label, false, {})
        }

        // Then, label is displayed
        testRule.onNodeWithText(label).assertIsDisplayed()
    }


    @Test
    fun selected_displaysCheckIcon() {
        // Given a selected filter list item
        testRule.setContent {
            FilterListItem(label, true, {})
        }

        // Then, label is displayed
        testRule.onNodeWithTag(CoreTestTags.FILTER_ITEM_CHECK, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun selected_assertIsSemanticallySelected() {
        // Given a selected filter list item
        testRule.setContent {
            FilterListItem(label, true, {}, modifier = Modifier.testTag(testTag))
        }
        // Then, the item is selected
        testRule.onNodeWithTag(testTag).assertIsSelected()
    }

    @Test
    fun unselected_assertIsNotSemanticallySelected() {
        // Given an unselected filter list item
        testRule.setContent {
            FilterListItem(label, false, {}, modifier = Modifier.testTag(testTag))
        }
        // Then, the item is not selected
        testRule.onNodeWithTag(testTag).assertIsNotSelected()
    }

    @Test
    fun onSelect_triggersCallback() {
        // Given a filter list item
        var callbackTriggered = false
        testRule.setContent {
            FilterListItem(
                label,
                false,
                { callbackTriggered = true },
                modifier = Modifier.testTag(testTag)
            )
        }

        // When the item is selected
        testRule.onNodeWithTag(testTag).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }
}