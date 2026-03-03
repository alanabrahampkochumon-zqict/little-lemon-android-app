package com.littlelemon.application.core.presentation.components

import android.app.Application
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CheckboxTest {


    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var application: Application

    private lateinit var iconMatcher: SemanticsMatcher

    @Before
    fun setUp() {
        application = RuntimeEnvironment.getApplication()
        iconMatcher = hasTestTag(application.getString(R.string.test_tag_checkbox_icon))
        composeTestRule.mainClock.autoAdvance = false
    }


    @Test
    fun checkbox_whenChecked_showsCheckIcon() {
        // Given a checkbox that is checked
        composeTestRule.setContent {
            Checkbox(checked = true)
        }
        composeTestRule.mainClock.advanceTimeBy(500L)
        composeTestRule.onAllNodes(iconMatcher).fetchSemanticsNodes().isNotEmpty()
    }

    @Test
    fun checkbox_whenUnchecked_doesNotShowCheckIcon() {
        // Given a checkbox that is not checked
        composeTestRule.setContent {
            Checkbox(checked = false)
        }

        // Then the icon is not displayed
        composeTestRule.onNode(iconMatcher).assertIsNotDisplayed()
    }

    @Test
    fun checkbox_withLabel_showsLabel() {
        // Given a checkbox with label
        val label = "Checkbox"
        composeTestRule.setContent {
            Checkbox(checked = true, label = label)
        }

        // Then the label is displayed
        composeTestRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun checkbox_whenClicked_triggersOnClickCallback() {
        // Given a checkbox that is checked
        var callbackTriggered = false
        val label = "Checkbox"
        composeTestRule.setContent {
            Checkbox(checked = true, onCheckedChange = { callbackTriggered = true }, label = label)
        }

        // When the checkbox is clicked
        composeTestRule.onNodeWithText(label).performClick()

        // Then the callback is triggered
        assertTrue(callbackTriggered)
    }


}