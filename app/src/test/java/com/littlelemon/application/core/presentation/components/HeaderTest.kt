package com.littlelemon.application.core.presentation.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HeaderTest {

    @get:Rule
    val testRule = createComposeRule()

    val label = "Test Header"
    val contentText = "Content"

    @Test
    fun displaysHeaderLabel() {
        // When a header is displayed with a label
        testRule.setContent {
            Header(label)
        }

        // Then, the label is displayed
        testRule.onNodeWithText(label).assertIsDisplayed()
    }


    @Test
    fun displaysContent() {
        // When a header is displayed with content
        testRule.setContent {
            Header(label) { Text(contentText) }
        }

        // Then, the content is displayed
        testRule.onNodeWithText(contentText).assertIsDisplayed()
    }

}