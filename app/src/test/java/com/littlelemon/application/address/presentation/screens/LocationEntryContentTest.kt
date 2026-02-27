package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class LocationEntryContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val application = RuntimeEnvironment.getApplication()

    private val labelLabelMatcher = hasText(application.getString(R.string.label_address_label))
    private val labelPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_label))

    private val buildingLabelMatcher =
        hasText(application.getString(R.string.label_address_building_name))
    private val buildingPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_building_name))

    private val streetAddressLabelMatcher =
        hasText(application.getString(R.string.label_address_street_address))
    private val streetAddressPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_street_address))

    private val cityLabelMatcher = hasText(application.getString(R.string.label_address_city))
    private val cityPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_city))

    private val stateLabelMatcher = hasText(application.getString(R.string.label_address_state))
    private val statePlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_state))

    private val pinCodeLabelMatcher = hasText(application.getString(R.string.label_address_pincode))
    private val pinCodePlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_pincode))

    private val saveAsDefaultLabelMatcher =
        hasText(application.getString(R.string.label_address_save_as_default))
    private val saveAsDefaultTestTag =
        hasTestTag(application.getString(R.string.test_tag_address_save_as_default))


    @Test
    fun locationEntryContent_label_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, "label" label and its field is displayed
        composeTestRule.onNode(labelLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(labelPlaceholderMatcher).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_buildingName_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, "building name" label and its field is displayed
        composeTestRule.onNode(buildingLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(buildingPlaceholderMatcher).performScrollTo().assertIsDisplayed()
    }


    @Test
    fun locationEntryContent_streetAddress_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "street address" label and its field is displayed
        composeTestRule.onNode(streetAddressLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(streetAddressPlaceholderMatcher).performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_city_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "city" label and its field is displayed
        composeTestRule.onNode(cityLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(cityPlaceholderMatcher).performScrollTo().assertIsDisplayed()
    }


    @Test
    fun locationEntryContent_state_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "state" label and its field is displayed
        composeTestRule.onNode(stateLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(statePlaceholderMatcher).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_pinCode_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "pincode" label and its field is displayed
        composeTestRule.onNode(pinCodeLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(pinCodePlaceholderMatcher).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_saveAsDefault_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "pincode" label and its field is displayed
        composeTestRule.onNode(saveAsDefaultLabelMatcher).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(saveAsDefaultTestTag).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_labelChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onLabelChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(labelPlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_buildingNameChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onBuildingNameChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(buildingPlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_streetAddressChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onStreetAddressChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(streetAddressPlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_cityChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onCityChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(cityPlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    @Test
    fun locationEntryContent_stateChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onStateChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(statePlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    @Test
    fun locationEntryContent_pinCodeChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onPinCodeChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(pinCodePlaceholderMatcher).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    // TODO: Add back test after implementation
//    @Test
//    fun locationEntryContent_saveAsDefaultChange_onChangeCallbackIsTriggered() {
//        // Given a location entry screen
//        var changedValue = false
//        composeTestRule.setContent {
//            LocationEntryContentRoot(
//                AddressState(),
//                onSaveAsDefaultChange = { changedValue = true })
//        }
//
//        // When text input is performed
//        composeTestRule.onNode(saveAsDefaultTestTag).performClick()
//
//        // Then, the callback is triggered
//        assertTrue(changedValue)
//    }

}