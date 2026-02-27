package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class LocationPermissionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    // Matchers
    private val headerMatcher = hasText(application.getString(R.string.heading_location_permission))
    private val bodyCopyMatcher = hasText(application.getString(R.string.body_location))

    private val enableLocationMatcher = hasText(application.getString(R.string.act_enable_location))
    private val enterLocationManuallyButtonMatcher =
        hasText(application.getString(R.string.act_enter_location_manually))

    private val locationDialogHeaderMatcher =
        hasText(application.getString(R.string.dialog_title_delivery_address_needed))
    private val locationDialogBodyMatcher =
        hasText(application.getString(R.string.dialog_body_delivery_address_needed))

    private val dialogAllowAccessButtonMatcher =
        hasText(application.getString(R.string.dialog_act_allow_access))
    private val dialogEnterManuallyButtonMatcher =
        hasText(application.getString(R.string.dialog_act_enter_manually))

    @Test
    fun locationScreen_whenViewed_showsHeaderBodyTextAndActions() {

        // Given the location screen is displayed
        composeTestRule.setContent {
            LocationPermissionScreenRoot(AddressState())
        }

        // Then, header is displayed
        composeTestRule.onNode(headerMatcher).assertIsDisplayed()
        // Body text is displayed
        composeTestRule.onNode(bodyCopyMatcher).assertIsDisplayed()
        // Enable Location button is displayed
        composeTestRule.onNode(enableLocationMatcher).assertIsDisplayed()
        // Enter manual location button is displayed
        composeTestRule.onNode(enterLocationManuallyButtonMatcher).assertIsDisplayed()
    }

    @Test
    fun locationScreen_whenEnableLocationIsPressed_enableLocationCallbackIsTriggered() {

        // Given the location screen is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationPermissionScreenRoot(
                AddressState(),
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
            LocationPermissionScreenRoot(
                AddressState(),
                onManualLocationClick = { callbackTriggered = true })
        }

        // When the enable location button is pressed
        composeTestRule.onNode(enterLocationManuallyButtonMatcher).performClick()

        // Then, enable location clicked callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun locationScreen_alertDisplayed_showsCorrectContent() {
        // Given, alert dialog is displayed
        composeTestRule.setContent {
            LocationPermissionScreenRoot(
                AddressState(showLocationDialog = true),
            )
        }

        // Then, it shows the correct title and text
        composeTestRule.onNode(locationDialogHeaderMatcher).assertIsDisplayed()
        composeTestRule.onNode(locationDialogBodyMatcher).assertIsDisplayed()
    }

    @Test
    fun locationScreen_alertDisplayedAndAllowAccessPressed_triggersAllowAccessCallback() {
        // Given, the alert dialog is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationPermissionScreenRoot(
                AddressState(showLocationDialog = true),
                onAllowLocationAccessClick = { callbackTriggered = true })
        }

        // When, the allow access button is pressed
        composeTestRule.onNode(dialogAllowAccessButtonMatcher).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun locationScreen_alertDisplayedAndEnterManuallyPressed_triggersEnterManuallyButtonCallback() {
        // Given, the alert dialog is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationPermissionScreenRoot(
                AddressState(showLocationDialog = true),
                onManualLocationClick = { callbackTriggered = true })
        }

        // When, the allow access button is pressed
        composeTestRule.onNode(dialogEnterManuallyButtonMatcher).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }
}