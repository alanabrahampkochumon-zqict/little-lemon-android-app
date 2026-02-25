package com.littlelemon.application.auth.presentation.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.test.espresso.Espresso
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.auth.presentation.LoginRoute
import com.littlelemon.application.auth.presentation.PersonalizationRoute
import com.littlelemon.application.auth.presentation.VerificationRoute
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
class AuthScreenTest {

    private val application = RuntimeEnvironment.getApplication()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val loaderMatcher = hasTestTag(application.getString(R.string.test_tag_loader))
    val sendingOTPLoaderMatcher =
        hasText(application.getString(R.string.loader_sending_verification_code))
    val verifyingLoaderMatcher =
        hasText(application.getString(R.string.loader_verifying_code))

    val sendButtonMatcher = hasText(application.getString(R.string.act_send_otp))
    val verifyNavMatcher = hasText(application.getString(R.string.nav_verify_email))
    val changeEmailMatcher = hasText(application.getString(R.string.act_change_email))
    val backButtonMatcher = hasTestTag(
        application.getString(R.string.test_tag_navigation_action_left)
    )
    val verifyButtonMatcher = hasText(application.getString(R.string.act_verify))
    val personalizeTextMatcher = hasText(application.getString(R.string.body_personalization))
    val personalizeFinishButtonMatcher = hasText(application.getString(R.string.act_go))

    @Test
    fun authScreen_onLoading_loaderIsShown() {
        // Given a state with isLoading set to true
        composeTestRule.setContent {
            AuthScreenRoot(AuthState(isLoading = true), rememberNavBackStack(LoginRoute))
        }

        // Then, loader is shown
        composeTestRule.onNode(loaderMatcher).assertIsDisplayed()
    }

    @Test
    fun authScreen_loginRoute_whenVerifyOTPButtonIsPressed_navigatesToVerifyScreen() = runTest {
        // Given auth screen with login content
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(LoginRoute)
            AuthScreenRoot(AuthState(enableSendButton = true), backStack, onSendOTP = {
                backStack.add(VerificationRoute)
            })
        }

        // When verify button is pressed
        composeTestRule.onNode(sendButtonMatcher).performClick()

        // Then, sendButton is not displayed and verify navigation is shown
        composeTestRule.onNode(sendButtonMatcher).assertIsNotDisplayed()
        composeTestRule.onNode(verifyNavMatcher).assertIsDisplayed()
        composeTestRule.onNode(verifyButtonMatcher).assertIsDisplayed()

    }

    @Test
    fun authScreen_verificationRoute_whenChangeEmailIsPressed_navigatesToLoginScreen() = runTest {
        // Given auth Screen with verification content
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(LoginRoute, VerificationRoute)
            AuthScreenRoot(AuthState(enableSendButton = true), backStack, onChangeEmail = {
                backStack.remove(VerificationRoute)
            })
        }

        // When verify button is pressed
        composeTestRule.onNode(changeEmailMatcher).performClick()

        // Then, verify navigation and verify button is not displayed
        composeTestRule.onNode(verifyNavMatcher).assertIsNotDisplayed()
        composeTestRule.onNode(verifyButtonMatcher).assertIsNotDisplayed()
        // Send otp button is displayed
        composeTestRule.onNode(sendButtonMatcher).assertIsDisplayed()

    }

    @Test
    fun authScreen_verificationRoute_backButtonIsPressed_navigatesToLoginScreen() = runTest {
        // Given auth Screen with verification content
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(VerificationRoute)
            AuthScreenRoot(AuthState(enableSendButton = true), backStack, onNavigateBack = {
                backStack.remove(VerificationRoute)
                backStack.add(LoginRoute)
            })
        }

        // When back button is pressed
        composeTestRule.onNode(backButtonMatcher).performClick()

        // Then, verify navigation and verify button is not displayed
        composeTestRule.onNode(verifyNavMatcher).assertIsNotDisplayed()
        composeTestRule.onNode(verifyButtonMatcher).assertIsNotDisplayed()
        // Send otp button is displayed
        composeTestRule.onNode(sendButtonMatcher).assertIsDisplayed()

    }

    @Test
    fun authScreen_verificationRoute_verifyButtonIsPressed_navigatesToPersonalizeScreen() =
        runTest {
            // Given auth Screen with personalize content
            composeTestRule.setContent {
                val backStack = rememberNavBackStack(VerificationRoute)
                AuthScreenRoot(AuthState(enableVerifyButton = true), backStack, onVerifyOTP = {
                    backStack.remove(VerificationRoute)
                    backStack.add(PersonalizationRoute)
                })
            }

            // When back button is pressed
            composeTestRule.onNode(verifyButtonMatcher).performClick()

            // Then, verify navigation and verify button is not displayed
            composeTestRule.onNode(verifyNavMatcher).assertIsNotDisplayed()
            composeTestRule.onNode(verifyButtonMatcher).assertIsNotDisplayed()
            // Send otp button is displayed
            composeTestRule.onNode(personalizeTextMatcher).assertIsDisplayed()
            composeTestRule.onNode(personalizeFinishButtonMatcher).assertIsDisplayed()
        }

    @Test
    fun authScreen_personalizationRoute_systemBackPressed_exitsApp() = runTest {
        // Given auth Screen with personalize content
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(PersonalizationRoute)
            AuthScreenRoot(
                AuthState(enableLetsGoButton = true),
                backStack
            )
        }

        // When system back is pressed
        Espresso.pressBackUnconditionally()

        // Then the activity is destroyed
        composeTestRule.activityRule.scenario.onActivity { activity ->
            assertTrue { activity.isFinishing || activity.isDestroyed }
        }

    }

    @Test
    fun authScreen_loginContent_loading_showsLoginLoaderContent() = runTest {
        // Given auth screen with login content
        // And is in the loading state
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(LoginRoute)
            AuthScreenRoot(AuthState(isLoading = true), backStack)
        }

        // Then login content for sending email is shown
        composeTestRule.onNode(sendingOTPLoaderMatcher).assertIsDisplayed()
    }

    @Test
    fun authScreen_verificationContent_loading_showsVerifyingLoaderContent() = runTest {
        // Given auth screen with login content
        // And is in the loading state
        composeTestRule.setContent {
            val backStack = rememberNavBackStack(VerificationRoute)
            AuthScreenRoot(AuthState(isLoading = true), backStack)
        }

        // Then login content for verifying is shown
        composeTestRule.onNode(verifyingLoaderMatcher).assertIsDisplayed()
    }

}