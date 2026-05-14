package com.littlelemon.application.orders.presentation.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue


@RunWith(RobolectricTestRunner::class)
class CheckoutScreenTest {


    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    @Test
    fun onClickBack_triggersCallback() = runTest {
        var callbackTriggered = false
        testRule.setContent {
            CheckoutScreenRoot({ callbackTriggered = true })
        }

        testRule.onNodeWithContentDescription(application.getString(R.string.back_to_cart))
            .performClick()

        assertTrue { callbackTriggered }
    }

}