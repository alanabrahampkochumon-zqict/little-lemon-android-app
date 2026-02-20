package com.littlelemon.application.auth.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@MediumTest
@RunWith(RobolectricTestRunner::class)
class AuthScreenTest {

    private val application = RuntimeEnvironment.getApplication()

    @get:Rule
    val composeTestRule = createComposeRule()

    val loaderMatcher = hasTestTag(application.getString(R.string.test_tag_loader))

    @Test
    fun authScreen_onLoading_loaderIsShown() {
        // Given a state with isLoading set to true
        composeTestRule.setContent {
            AuthScreenRoot(AuthState(isLoading = true))
        }

        // Then, loader is shown
        composeTestRule.onNode(loaderMatcher).assertIsDisplayed()
    }

}