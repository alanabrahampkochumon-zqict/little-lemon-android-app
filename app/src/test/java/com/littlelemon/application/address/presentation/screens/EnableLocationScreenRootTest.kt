package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class EnableLocationScreenRootTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    // Matchers
    private val headerMatcher = hasText(application.getString(R.string.heading_location_permission))
    private val bodyCopyMatcher = hasText(application.getString(R.string.body_location))

    private val enableLocationMatcher = hasText(application.getString(R.string.act_enable_location))
    private val enterLocationManuallyMatcher =
        hasText(application.getString(R.string.act_enter_location_manually))

    @Test
    fun locationScreen_whenViewed_showsHeaderBodyTextAndActions() {

        // Given the location screen is displayed
        composeTestRule.setContent {
            EnableLocationScreenRoot()
        }

        // Then, header is displayed
        composeTestRule.onNode(headerMatcher).assertIsDisplayed()
        // Body text is displayed
        composeTestRule.onNode(bodyCopyMatcher).assertIsDisplayed()
        // Enable Location button is displayed
        composeTestRule.onNode(enableLocationMatcher).assertIsDisplayed()
        // Enter manual location button is displayed
        composeTestRule.onNode(enterLocationManuallyMatcher).assertIsDisplayed()
    }

    @Test
    fun locationScreen_whenEnableLocationIsPressed_enableLocationCallbackIsTriggered() {

        // Given the location screen is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            EnableLocationScreenRoot(

                onEnableLocationClick = { callbackTriggered = true })
        }

        // When the enable location button is pressed
        composeTestRule.onNode(enableLocationMatcher).performClick()

        // Then, enable location clicked callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun locationScreen_whenManualLocationIsPressed_manualLocationCallbackIsTriggered() {

        // Given the location screen is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            EnableLocationScreenRoot(

                onManualLocationClick = { callbackTriggered = true })
        }

        // When the enable location button is pressed
        composeTestRule.onNode(enterLocationManuallyMatcher).performClick()

        // Then, enable location clicked callback is triggered
        assertTrue(callbackTriggered)
    }

}