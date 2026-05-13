package com.littlelemon.application.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.home.HomeTestTags
import com.littlelemon.application.home.presentation.components.TopAppBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class TopAppBarTest {

    @get:Rule
    val testRule = createComposeRule()

    private val localLocation = LocalAddress(location = LocalLocation(1.234, 5.328))

    @Test
    fun displaysLogo() {
        // When a top app bar is displayed
        testRule.setContent {
            TopAppBar(
                localLocation, addressLoading = false, addressError = null, onAddressClick = {},
            )
        }

        // Then, the logo is displayed
        testRule.onNodeWithTag(HomeTestTags.LOGO).assertIsDisplayed()
    }
    
    @Test
    fun addressBarClicked_triggersCallback() {
        // Given a top app bar
        var callbackTriggered = false
        testRule.setContent {
            TopAppBar(
                localLocation,
                addressLoading = false,
                addressError = null,
                onAddressClick = { callbackTriggered = true },
            )
        }

        // When, the address bar is clicked
        testRule.onNodeWithTag(HomeTestTags.ADDRESS_BAR).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }
}