package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.address.presentation.AddressTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FloatingActionBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun floatingActionBar_textIsDisplayed() {
        // When a title is passed to floating action bar
        val title = "Title"
        composeTestRule.setContent {
            FloatingActionBar(title, {})
        }

        // Then, the text is displayed
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun floatingActionBar_onButtonPress_triggersCallback() {
        // Given a floating action bar
        val title = "Title"
        var callbackTriggered = false
        composeTestRule.setContent {
            FloatingActionBar(title, { callbackTriggered = true })
        }

        // When close button is clicked
        composeTestRule.onNodeWithTag(AddressTestTags.HEADER_CLOSE_BUTTON).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }
}