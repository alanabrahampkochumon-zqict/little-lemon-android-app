package com.littlelemon.application.shared.address.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.PhysicalAddress
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class AddressCardTest {

    @get:Rule
    val testRule = createComposeRule()
    private val application = RuntimeEnvironment.getApplication()

    val address = LocalAddress(
        id = "1234", label = "Physical Address", address = PhysicalAddress(
            address = "Some street",
            streetAddress = "some street address",
            city = "Some city",
            state = "Some state",
            pinCode = "12349324"
        ), isDefault = false
    )

    @Test
    fun defaultAddress_makeDefaultButton_isNotDisplayed() {
        testRule.setContent {
            AddressCard(
                address.copy(isDefault = true),
                onDeleteAddress = {},
                onMakeDefault = { },
                onEditAddress = {})
        }

        testRule.onNodeWithText(application.getString(R.string.make_default)).assertIsNotDisplayed()
    }

    @Test
    fun defaultAddress_defaultTag_isDisplayed() {
        testRule.setContent {
            AddressCard(
                address.copy(isDefault = true),
                onDeleteAddress = {},
                onMakeDefault = { },
                onEditAddress = {})
        }

        testRule.onNodeWithText(application.getString(R.string.default_tag)).assertIsDisplayed()
    }

    @Test
    fun makeDefaultButton_triggersCallback() {
        var callbackTriggered = false
        testRule.setContent {
            AddressCard(
                address,
                onDeleteAddress = {},
                onMakeDefault = { callbackTriggered = true },
                onEditAddress = {})
        }

        testRule.onNodeWithText(application.getString(R.string.make_default)).performClick()

        assertTrue { callbackTriggered }
    }


    @Test
    fun editAddressButton_triggersCallback() {
        var callbackTriggered = false
        testRule.setContent {
            AddressCard(
                address,
                onDeleteAddress = {},
                onMakeDefault = { },
                onEditAddress = { callbackTriggered = true })
        }

        testRule.onNodeWithContentDescription(application.getString(R.string.edit_address))
            .performClick()

        assertTrue { callbackTriggered }
    }

    @Test
    fun editDeleteButton_triggersCallback() {
        var callbackTriggered = false
        testRule.setContent {
            AddressCard(
                address,
                onDeleteAddress = { callbackTriggered = true },
                onMakeDefault = { },
                onEditAddress = { })
        }

        testRule.onNodeWithContentDescription(application.getString(R.string.delete_address))
            .performClick()

        assertTrue { callbackTriggered }
    }

}