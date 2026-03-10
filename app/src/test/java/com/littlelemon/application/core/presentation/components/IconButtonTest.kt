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

    @Test
    fun primaryIconButton_whenPressed_triggersCallback() {
        // Given an enabled primary icon button
        var callbackTriggered = false
        composeTestRule.setContent {
            PrimaryIconButton(
                R.drawable.ic_plus, { callbackTriggered = true },
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
    fun primaryIconButton_whenNotEnabled_isDisabled() {
        // Given a primary icon button with enabled = false
        composeTestRule.setContent {
            PrimaryIconButton(
                R.drawable.ic_plus, { },
                iconDescription = buttonDescription,
                enabled = false
            )
        }

        // Then, the button is not enabled
        composeTestRule.onNodeWithContentDescription(buttonDescription).assertIsNotEnabled()
    }

    @Test
    fun secondaryIconButton_whenPressed_triggersCallback() {
        // Given an enabled secondary icon button
        var callbackTriggered = false
        composeTestRule.setContent {
            SecondaryIconButton(
                R.drawable.ic_plus, { callbackTriggered = true },
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
    fun secondaryIconButton_whenNotEnabled_isDisabled() {
        // Given a secondary icon button with enabled = false
        composeTestRule.setContent {
            SecondaryIconButton(
                R.drawable.ic_plus, { },
                iconDescription = buttonDescription,
                enabled = false
            )
        }

        // Then, the button is not enabled
        composeTestRule.onNodeWithContentDescription(buttonDescription).assertIsNotEnabled()
    }

    @Test
    fun destructiveIconButton_whenPressed_triggersCallback() {
        // Given an enabled destructive icon button
        var callbackTriggered = false
        composeTestRule.setContent {
            DestructiveIconButton(
                R.drawable.ic_plus, { callbackTriggered = true },
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
    fun destructiveIconButton_whenNotEnabled_isDisabled() {
        // Given a destructive icon button with enabled = false
        composeTestRule.setContent {
            SecondaryIconButton(
                R.drawable.ic_plus, { },
                iconDescription = buttonDescription,
                enabled = false
            )
        }

        // Then, the button is not enabled
        composeTestRule.onNodeWithContentDescription(buttonDescription).assertIsNotEnabled()
    }


}