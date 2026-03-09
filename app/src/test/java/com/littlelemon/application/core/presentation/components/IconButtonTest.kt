package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class IconButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testButtonColor = IconButtonColors(
        backgroundColor = Color.White,
        contentColor = Color.White,
        disabledBackgroundColor = Color.DarkGray,
        disabledContentColor = Color.LightGray
    )
    private val buttonDescription = "Button"


    @Test
    fun basicIconButton_whenPressed_triggersCallback() {
        // Given an enabled icon button
        var callbackTriggered = false
        composeTestRule.setContent {
            BasicIconButton(
                R.drawable.ic_plus, { callbackTriggered = true },
                colors = testButtonColor,
                iconDescription = buttonDescription,
                enabled = true
            )
        }

        // When pressed
        composeTestRule.onNodeWithContentDescription(buttonDescription).performClick()

        // Then, it triggers callback
        assertTrue(callbackTriggered)

    }

    @Test
    fun basicIconButton_whenNotEnabled_isDisabled() {
        // Given an icon button with enabled = false
        composeTestRule.setContent {
            BasicIconButton(
                R.drawable.ic_plus, { },
                colors = testButtonColor,
                iconDescription = buttonDescription,
                enabled = false
            )
        }

        // Then, the button is not enabled
        composeTestRule.onNodeWithContentDescription(buttonDescription).assertIsNotEnabled()
    }

}