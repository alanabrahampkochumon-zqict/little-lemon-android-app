package com.littlelemon.application.auth.presentation

import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.utils.MainTestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MainTestDispatcherRule::class)
class AuthViewModelTest {


    @Nested
    inner class StateModificationTests {

        val viewModel = AuthViewModel()

        @Test
        fun onEmailChange_toValidEmail_stateUpdatesToNewEmail() = runTest {
            // Arrange
            val newText = "test@gmail.com"

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = newText))

            // Assert
            assertEquals(newText, viewModel.state.value.email)
        }

//        @Test
//        fun on
    }
}