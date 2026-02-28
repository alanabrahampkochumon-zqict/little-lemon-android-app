package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.google.testing.junit.testparameterinjector.TestParameter
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.utils.ComposeMatcherHelper
import com.littlelemon.application.utils.MatcherType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestParameterInjector
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals

@RunWith(RobolectricTestParameterInjector::class)
class LocationEntryContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val application by lazy { RuntimeEnvironment.getApplication() }
    val labelLabelMatcher = hasText(application.getString(R.string.label_address_label))
    val labelPlaceholderMatcher =
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

    private val pinCodeLabelMatcher =
        hasText(application.getString(R.string.label_address_pincode))
    private val pinCodePlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_pincode))

    private val saveAsDefaultLabelMatcher =
        hasText(application.getString(R.string.label_address_save_as_default))
    private val saveAsDefaultTestTag =
        hasTestTag(application.getString(R.string.test_tag_address_save_as_default))

    private val composeMatchHelper = ComposeMatcherHelper(application)

    enum class ContentMatchTestCase(
        val label: Int,
        val placeholder: Int,
        val labelMatcherType: MatcherType = MatcherType.Text,
        val placeholderMatcherType: MatcherType = MatcherType.Text
    ) {
        LABEL(R.string.label_address_label, R.string.placeholder_address_label),
        BUILDING_NAME(
            R.string.label_address_building_name,
            R.string.placeholder_address_building_name
        ),
        STREET_ADDRESS(
            R.string.label_address_street_address,
            R.string.placeholder_address_street_address
        ),
        CITY(R.string.label_address_city, R.string.placeholder_address_city),
        STATE(R.string.label_address_state, R.string.placeholder_address_state),
        PIN_CODE(R.string.label_address_pincode, R.string.placeholder_address_pincode),
        SAVE_AS_DEFAULT(
            R.string.label_address_save_as_default,
            R.string.test_tag_address_save_as_default,
            placeholderMatcherType = MatcherType.TestTag
        )
    }

    @Test
    fun locationEntryContent_fields_areDisplayed(
        @TestParameter case: ContentMatchTestCase
    ) {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, an input field's label and its input is displayed
        composeTestRule.onNode(composeMatchHelper.getMatcher(case.label, case.labelMatcherType))
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                case.placeholder,
                case.placeholderMatcherType
            )
        ).performScrollTo()
            .assertIsDisplayed()
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