package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
class BadgeTest {

    @get:Rule
    val testRule = createComposeRule()

    private val label = "Badge"
    private val testTag = "TestTag"


    @Test
    fun displaysLabel() {
        testRule.setContent {
            BasicBadge(label, false, {})
        }

        testRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun selected_displaysIcon() {
        testRule.setContent {
            BasicBadge(label, true, {})
        }

        testRule.onNodeWithTag(CoreTestTags.BADGE_ICON, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun unselected_doesNotDisplayIcon() {
        testRule.setContent {
            BasicBadge(label, false, {})
        }

        testRule.onNodeWithTag(CoreTestTags.BADGE_ICON, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun selected_IsSemanticallySelected() {
        testRule.setContent {
            BasicBadge(label, true, {}, modifier = Modifier.testTag(testTag))
        }
        testRule.onNodeWithTag(testTag).assertIsSelected()
    }

    @Test
    fun unselected_IsSemanticallyUnselected() {
        testRule.setContent {
            BasicBadge(label, false, {}, modifier = Modifier.testTag(testTag))
        }
        testRule.onNodeWithTag(testTag)
            .assertIsNotSelected()
    }

    @Test
    fun onSelect_triggersCallback() {
        var callbackTriggered = false

        // Given a badge
        testRule.setContent {
            BasicBadge(
                label,
                false,
                { callbackTriggered = true },
                modifier = Modifier.testTag(testTag)
            )
        }

        // When clicked
        testRule.onNodeWithTag(testTag)
            .performClick()
        
        // Then it triggers callback
        assertTrue(callbackTriggered)
    }


}