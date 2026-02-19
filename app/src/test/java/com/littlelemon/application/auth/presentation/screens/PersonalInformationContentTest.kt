package com.littlelemon.application.auth.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PersonalInformationContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    val letGoButtonText by lazy { application.getString(R.string.act_go) }
    val firstNamePlaceholder by lazy { application.getString(R.string.placeholder_first_name) }
    val lastNamePlaceholder by lazy { application.getString(R.string.placeholder_last_name) }


    val firstName = "Robert"
    val lastName = "Philips"

    @Test
    fun personalInformationScreen_byDefault_haveDefaultPlaceholdersAndButtonIsDisabled() {

        // Given PI Screen without any interaction
        composeTestRule.setContent {
            PersonalInformationContent(AuthState())
        }

        // Then, placeholder texts for first name and last are shown
        composeTestRule.onNodeWithText(firstNamePlaceholder).assertIsDisplayed()
        composeTestRule.onNodeWithText(lastNamePlaceholder).assertIsDisplayed()
        composeTestRule.onNodeWithText(letGoButtonText).assertIsNotEnabled()
    }

    @Test
    fun personalInformationScreen_withValidFirstName_firstNameIsDisplayedAndButtonIsDisabled() {
        // Given PI Screen with only firstname
        composeTestRule.setContent {
            PersonalInformationContent(AuthState(enableLetsGoButton = true, firstName = firstName))
        }

        // Then, firstname is displayed and let's go button is enabled
        composeTestRule.onNodeWithText(firstName).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            letGoButtonText
        ).assertIsEnabled()
    }

    @Test
    fun personalInformationScreen_withValidFirstNameAndLastName_firstNameIsDisplayedAndButtonIsDisabled() {
        // Given PI Screen with only firstname
        composeTestRule.setContent {
            PersonalInformationContent(
                AuthState(
                    enableLetsGoButton = true,
                    firstName = firstName,
                    lastName = lastName
                )
            )
        }

        // Then, firstname and lastname is displayed and let's go button is enabled
        composeTestRule.onNodeWithText(firstName).assertIsDisplayed()
        composeTestRule.onNodeWithText(lastName).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            letGoButtonText
        ).assertIsEnabled()
    }

    @Test
    fun personalInformationScreen_whenFirstNameIsEntered_triggersOnChangeCallback() {
        var newFirstName = ""

        // Given PI Screen with only firstname
        composeTestRule.setContent {
            PersonalInformationContent(AuthState(), onFirstNameChange = { newFirstName = it })
        }

        // When firstname is changed
        composeTestRule.onNodeWithText(firstNamePlaceholder).performTextInput(firstName)

        // Then, first name change callback is called
        assertEquals(firstName, newFirstName)
    }

    @Test
    fun personalInformationScreen_whenLastNameIsEntered_triggersOnChangeCallback() {
        var newLastName = ""

        // Given PI Screen with only lastname
        composeTestRule.setContent {
            PersonalInformationContent(
                AuthState(),
                onLastNameChange = { newLastName = it; })
        }

        // When lastname is changed
        composeTestRule.onNodeWithText(lastNamePlaceholder).performTextInput(lastName)

        // Then, lastname change callback is called
        assertEquals(lastName, newLastName)
    }

    @Test
    fun personalInformationScreen_whenLetsGoButtonIsPressed_triggersOnClickCallback() {
        var buttonClicked = false

        // Given PI Screen with only firstname
        composeTestRule.setContent {
            PersonalInformationContent(
                AuthState(enableLetsGoButton = true),
                onComplete = { buttonClicked = true })
        }

        // When button is clicked
        composeTestRule.onNodeWithText(letGoButtonText).performClick()

        // Then, on complete callback is triggered
        assertTrue(buttonClicked)
    }
}