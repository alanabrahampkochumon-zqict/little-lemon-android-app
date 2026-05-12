package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.AddressPicker
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class AddressPickerTest {

    @get:Rule
    val testRule = createComposeRule()

    private val currentAddress = "address 1"
    private val application = RuntimeEnvironment.getApplication()
    private val deliveringTo = R.string.label_delivering
    private val notDeliverableTo = R.string.label_not_delivering

    @Test
    fun displaysAddress() {
        // Given an address picker
        testRule.setContent {
            AddressPicker(
                address = currentAddress, onAddressChange = {}
            )
        }

        // Then, address is displayed
        testRule.onNodeWithText(currentAddress).assertIsDisplayed()
    }

    @Test
    fun deliverable_displaysDeliveringTo() {
        // Given an address picker with deliverable set to true
        testRule.setContent {
            AddressPicker(
                address = currentAddress,
                deliverable = true, onAddressChange = {}
            )
        }

        // Then, delivery to headline is displayed
        testRule.onNodeWithText(application.getString(deliveringTo)).assertIsDisplayed()
    }

    @Test
    fun nonDeliverable_displayNotDeliverableTo() {
        // Given an address picker with deliverable set to true
        testRule.setContent {
            AddressPicker(
                address = currentAddress,
                deliverable = false, onAddressChange = {}
            )
        }

        // Then, delivery to headline is displayed
        testRule.onNodeWithText(application.getString(notDeliverableTo)).assertIsDisplayed()
    }

    @Test
    fun onAddressChange_triggersCallback() {
        // Given an address picker with deliverable set to true
        var callbackTriggered = false
        testRule.setContent {
            AddressPicker(
                address = currentAddress,
                deliverable = false, onAddressChange = { callbackTriggered = true }
            )
        }

        // Then, delivery to headline is displayed
        testRule.onNodeWithText(currentAddress).performClick()

        assertTrue { callbackTriggered }
    }

}