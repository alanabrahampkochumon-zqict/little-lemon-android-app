package com.littlelemon.application.auth.presentation

import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.utils.MainTestDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MainTestDispatcherRule::class)
class AuthViewModelTest {

    companion object {
        private const val DEBOUNCE_DELAY_MS = 1000L
    }

    @Nested
    inner class StateModificationTests {

        val viewModel = AuthViewModel()

        @Test
        fun onEmailChange_toIncompleteValue_stateUpdatesEmailWithoutErrorMessage() {
            // Arrange
            val newText = "test"

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = newText))

            // Assert
            assertEquals(newText, viewModel.state.value.email)
            assertFalse(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @Test
        fun onEmailChange_toValidEmail_stateUpdatesToNewEmailAndEnableSendButton() = runTest {
            // Arrange
            val newText = "test@gmail.com"

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = newText))

            // Assert
            assertEquals(newText, viewModel.state.value.email)
            assertTrue(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onEmailChange_toInvalidEmail_stateUpdatesErrorMessageWithDelayAndEnableSendButton() =
            runTest {
                // Arrange
                val newText = "invalid_email.com"

                // Act
                viewModel.onAction(AuthActions.ChangeEmail(email = newText))

                // Assert
                assertEquals(newText, viewModel.state.value.email)
                assertFalse(viewModel.state.value.enableSendButton)
                assertNull(viewModel.state.value.emailError)


                advanceTimeBy(DEBOUNCE_DELAY_MS)

                assertNotNull(viewModel.state.value.emailError)
            }

        @Test
        fun onOTPChange_toValidOTP_stateUpdatesToNewOTP() = runTest {
            // Arrange
            val newOTP = listOf(3, 1, 6, 3, 1, 6)

            // Act
            viewModel.onAction(AuthActions.ChangeOTP(otp = newOTP))

            // Assert
            assertEquals(newOTP, viewModel.state.value.oneTimePassword)
        }

        @Test
        fun onFirstNameChange_toValidFirstName_stateUpdatesToNewFirstName() = runTest {
            // Arrange
            val firstName = "First Name"

            // Act
            viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))

            // Assert
            assertEquals(firstName, viewModel.state.value.firstName)
        }


        @Test
        fun onLastNameChange_toValidLastName_stateUpdatesToNewLastName() = runTest {
            // Arrange
            val lastName = "Last Name"

            // Act
            viewModel.onAction(AuthActions.ChangeLastName(lastName = lastName))

            // Assert
            assertEquals(lastName, viewModel.state.value.lastName)
        }
    }
}