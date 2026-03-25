package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

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
                address = currentAddress
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
                deliverable = true
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
                deliverable = false
            )
        }

        // Then, delivery to headline is displayed
        testRule.onNodeWithText(application.getString(notDeliverableTo)).assertIsDisplayed()
    }

}