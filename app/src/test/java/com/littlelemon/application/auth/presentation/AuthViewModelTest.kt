package com.littlelemon.application.auth.presentation

import app.cash.turbine.test
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateOTPUseCase
import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationResult
import com.littlelemon.application.utils.MainTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

    private val validateEmailUseCase = mockk<ValidateEmailUseCase>(relaxed = true)
    private val validateOTPUseCase = mockk<ValidateOTPUseCase>()
    val viewModel = AuthViewModel(validateEmailUseCase, validateOTPUseCase)

    @Nested
    inner class EmailTests {

        @Test
        fun onEmailChange_toIncompleteValue_stateUpdatesEmailWithoutErrorMessage() = runTest {
            // Arrange
            val email = "test"
            val error = ValidationError.InvalidFormat
            val message = "invalid email"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                error, message
            )

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = email))

            // Assert
            assertEquals(email, viewModel.state.value.email)
            assertFalse(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @Test
        fun onEmailChange_toValidEmail_stateUpdatesToNewEmailAndEnableSendButton() = runTest {
            // Arrange
            val email = "test@gmail.com"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Success

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = email))

            // Assert
            assertEquals(email, viewModel.state.value.email)
            assertTrue(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onEmailChange_toInvalidEmail_stateUpdatesErrorMessageWithDelayAndEnableSendButton() =
            runTest {
                // Arrange
                Dispatchers.setMain(StandardTestDispatcher(testScheduler))
                val scopedViewModel =
                    AuthViewModel(
                        validateEmailUseCase,
                        validateOTPUseCase
                    ) // Create VM to use the StandardTestDispatcher

                val email = "invalid_email.com"
                val error = ValidationError.InvalidFormat
                val message = "invalid email"
                coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                    error, message
                )
                scopedViewModel.state.test {
                    skipItems(1)

                    // Act
                    scopedViewModel.onAction(AuthActions.ChangeEmail(email = email))

                    // Assert
                    val firstState = awaitItem()
                    assertEquals(email, firstState.email)
                    assertFalse(firstState.enableSendButton)
                    assertNull(firstState.emailError)

                    advanceTimeBy(DEBOUNCE_DELAY_MS)

                    // Assert II
                    val delayedState = awaitItem()
                    assertNotNull(delayedState.emailError)
                }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onEmailChange_afterInvalidEmailEntered_clearsErrorMessage() = runTest {
            // Arrange
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val scopeViewModel = AuthViewModel(validateEmailUseCase, validateOTPUseCase)

            val email = "invalid_email.com"
            val updatedEmail = "test"

            val error = ValidationError.InvalidFormat
            val message = "invalid email"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                error, message
            )
            coEvery { validateEmailUseCase.invoke(updatedEmail) } returns ValidationResult.Failure(
                error,
                message
            )

            scopeViewModel.state.test {
                skipItems(1)

                // Act
                scopeViewModel.onAction(AuthActions.ChangeEmail(email = email))

                // Assert
                val firstState = awaitItem()
                assertEquals(email, firstState.email)
                assertFalse(firstState.enableSendButton)
                assertNull(firstState.emailError)

                advanceTimeBy(DEBOUNCE_DELAY_MS)
                val debouncedState = awaitItem()

                assertNotNull(debouncedState.emailError)

                // Act II
                scopeViewModel.onAction(AuthActions.ChangeEmail(email = updatedEmail))
                val updatedState = awaitItem()

                // Assert
                assertEquals(updatedEmail, updatedState.email)
                assertFalse(updatedState.enableSendButton)
                assertNull(updatedState.emailError)
            }


        }
    }

    @Nested
    inner class OTPTests {
        @Test
        fun onOTPChange_toValidOTP_stateUpdatesToNewOTP() = runTest {
            // Arrange
            val newOTP = listOf(3, 1, 6, 3, 1, 6)
            coEvery { validateOTPUseCase.invoke(newOTP.joinToString("")) } returns ValidationResult.Success

            // Act
            viewModel.onAction(AuthActions.ChangeOTP(otp = newOTP))

            // Assert
            assertEquals(newOTP, viewModel.state.value.oneTimePassword)
            assertTrue(viewModel.state.value.enableVerifyButton)
            assertNull(viewModel.state.value.otpError)
        }


        // This test covers when user deletes an invalid otp as well.
        @Test
        fun onOTPChange_toIncompleteOTP_verifyButtonIsDisabled() = runTest {
            // Arrange
            val error = ValidationError.InvalidFormat
            val message = "invalid otp"
            val newOTP = listOf(3, 1, 6, 3, 1)
            coEvery { validateOTPUseCase.invoke(newOTP.joinToString("")) } returns ValidationResult.Failure(
                error, message
            )

            // Act
            viewModel.onAction(AuthActions.ChangeOTP(otp = newOTP))

            // Assert
            assertEquals(newOTP, viewModel.state.value.oneTimePassword)
            assertFalse(viewModel.state.value.enableVerifyButton)
            assertNull(viewModel.state.value.otpError)
        }
    }


    @Nested
    inner class NameTests {

        @Test
        fun onFirstNameChange_toEmptyString_buttonIsDisabled() = runTest {
            // Arrange
            val firstName = ""

            // Act
            viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))

            // Assert
            assertEquals(firstName, viewModel.state.value.firstName)
            assertFalse(viewModel.state.value.enableLetsGoButton)
        }

        @Test
        fun onFirstNameChange_toValidFirstName_stateUpdatesToNewFirstName() = runTest {
            // Arrange
            val firstName = "First Name"

            // Act
            viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))

            // Assert
            assertEquals(firstName, viewModel.state.value.firstName)
            assertTrue(viewModel.state.value.enableLetsGoButton)
        }


        @Test
        fun onLastNameChangeAndValidFirstName_toValidLastName_stateUpdatesToNewLastNameWithButtonEnabled() =
            runTest {
                // Arrange
                val lastName = "Last Name"
                val firstName = "First Name"

                // Act
                viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))
                viewModel.onAction(AuthActions.ChangeLastName(lastName = lastName))

                // Assert
                assertEquals(lastName, viewModel.state.value.lastName)
                assertTrue(viewModel.state.value.enableLetsGoButton)

            }
    }
}