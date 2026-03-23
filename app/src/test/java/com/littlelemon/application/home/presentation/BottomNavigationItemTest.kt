package com.littlelemon.application.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class BottomNavigationItemTest {

    @get:Rule
    val testRule = createComposeRule()

    val drawable = R.drawable.ic_launcher_foreground
    val label = "Menu Item"

    @Test
    fun displaysLabel() {
        // Given a bottom navigation item
        testRule.setContent {
            BottomNavigationItem(drawable, label, true, {})
        }

        // Then label is displayed
        testRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun displaysIcon() {
        // Given a bottom navigation item
        testRule.setContent {
            BottomNavigationItem(drawable, label, true, {})
        }

        // Then label is displayed
        testRule.onNodeWithContentDescription(label).assertIsDisplayed()
    }

    @Test
    fun onSelect_triggersCallback() {
        // Given a bottom navigation item
        var callbackTriggered = false
        testRule.setContent {
            BottomNavigationItem(drawable, label, true, { callbackTriggered = true })
        }

        // When the icon is clicked
        testRule.onNodeWithText(label).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

}