package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
class ActionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val primaryActionText = "Primary action"
    val secondaryActionText = "Secondary Action"
    val heading = "heading"
    val content = "This is some content"


    val illustrationTag = CoreTestTags.ACTION_SCREEN_ILLUSTRATION


    // TODO: Replace with parameterized tests
    @Test
    fun actionScreen_correctlyDisplaysContent() {
        // When the action screen is visible
        composeTestRule.setContent {
            ActionScreen(
                primaryActionText, secondaryActionText, {}, {},
                illustration = R.drawable.ic_launcher_background,
                heading = heading,
                content = content,
            )
        }

        // Then the contents are displayed
        composeTestRule.onNodeWithText(content).assertIsDisplayed()
        composeTestRule.onNodeWithText(heading).assertIsDisplayed()
        composeTestRule.onNodeWithText(primaryActionText).assertIsDisplayed()
        composeTestRule.onNodeWithText(secondaryActionText).assertIsDisplayed()
    }

    @Test
    fun actionScreen_displaysIllustration() {
        // When the action screen is visible
        composeTestRule.setContent {
            ActionScreen(
                primaryActionText, secondaryActionText, {}, {},
                illustration = R.drawable.ic_launcher_background,
                heading = heading,
                content = content,
            )
        }

        // Then the contents are displayed
        composeTestRule.onNodeWithTag(illustrationTag).assertIsDisplayed()
    }

    @Test
    fun actionScreen_primaryActionClick_triggersCallback() {
        // Given an action screen
        var callbackTriggered = false
        composeTestRule.setContent {
            ActionScreen(
                primaryActionText, secondaryActionText, { callbackTriggered = true }, {},
                illustration = R.drawable.ic_launcher_background,
                heading = heading,
                content = content,
            )
        }

        // When the primary action button is clicked
        composeTestRule.onNodeWithText(primaryActionText).performClick()

        // Then the callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun actionScreen_secondaryActionClick_triggersCallback() {
        // Given an action screen
        var callbackTriggered = false
        composeTestRule.setContent {
            ActionScreen(
                primaryActionText, secondaryActionText, { }, { callbackTriggered = true },
                illustration = R.drawable.ic_launcher_background,
                heading = heading,
                content = content,
            )
        }

        // When the secondary action button is clicked
        composeTestRule.onNodeWithText(secondaryActionText).performClick()

        // Then the callback is triggered
        assertTrue(callbackTriggered)
    }

}