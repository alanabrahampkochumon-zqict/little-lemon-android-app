package com.littlelemon.application.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.littlelemon.application.home.HomeTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class TopAppBarTest {

    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun displaysLogo() {
        // When a top app bar is displayed
        testRule.setContent {
            TopAppBar({})
        }

        // Then, the logo is displayed
        testRule.onNodeWithTag(HomeTestTags.LOGO).assertIsDisplayed()
    }

    @Test
    fun displaysSearchButton() {
        // When a top app bar is displayed
        testRule.setContent {
            TopAppBar({})
        }

        // Then, the search button is displayed
        testRule.onNodeWithTag(HomeTestTags.SEARCH_BUTTON).assertIsDisplayed()

    }

    @Test
    fun searchButtonClick_triggersCallback() {
        // Given a top app bar
        var callbackTriggered = false
        testRule.setContent {
            TopAppBar({ callbackTriggered = true })
        }

        // When, the search button is pressed
        testRule.onNodeWithTag(HomeTestTags.SEARCH_BUTTON).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }
}