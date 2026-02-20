package com.littlelemon.application.core.presentation.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
class LoaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loader_whenDisplayed_doesNotAllowTouchEventsToPropagate() {
        // Given the loader is displayed
        val buttonLabel = "Test Button"
        var onClicked = false
        composeTestRule.setContent {
            Loader(showLoader = true) {
                Button(buttonLabel, onClick = { onClicked = true })
            }
        }

        // When the button is clicked
        composeTestRule.onNodeWithText(buttonLabel).performClick()

        // Then, button callback is not triggered
        assertFalse(onClicked)
    }

    @Test
    fun loader_whenNotDisplayed_allowsTouchEventsToPropagate() {
        // Given the loader is not displayed
        val buttonLabel = "Test Button"
        var onClicked = false
        composeTestRule.setContent {
            Loader(showLoader = false) {
                Button(buttonLabel, onClick = { onClicked = true })
            }
        }

        // When the button is clicked
        composeTestRule.onNodeWithText(buttonLabel).performClick()

        // Then, button callback is not triggered
        assertTrue(onClicked)
    }

    @Test
    fun loader_whenDisplayed_showsCorrectLoadingContent() {
        // Given the loader is displayed
        val buttonLabel = "Test Button"
        val contentText = "Content Text"
        composeTestRule.setContent {
            Loader(showLoader = true, loaderContent = { Text(contentText) }) {
                Button(buttonLabel, onClick = {})
            }
        }

        // Then the loader content is displayed
        composeTestRule.onNodeWithText(contentText).assertIsDisplayed()
    }
}