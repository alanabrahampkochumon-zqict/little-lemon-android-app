package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.core.CoreTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MiniLoaderTest {

    @get:Rule
    val testRule = createComposeRule()

    private val text = "Some loading text..."

    @Test
    fun displaysLabel() {
        // Given a mini loader
        testRule.setContent {
            MiniLoader(text)
        }

        // Then, loading text is displayed
        testRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun displaysSpinner() {
        // Given a mini loader
        testRule.setContent {
            MiniLoader(text)
        }

        // Then, progress indicator is displayed
        testRule.onNodeWithTag(CoreTestTags.PROGRESS_INDICATOR).assertIsDisplayed()
    }

}