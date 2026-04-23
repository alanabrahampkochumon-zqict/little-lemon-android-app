package com.littlelemon.application.home.presentation.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.core.CoreTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
class NavigationSheetTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()


    @Test
    fun displaysLogo() {
        testRule.setContent {
            NavigationSheet({})
        }
        testRule.onNodeWithTag(CoreTestTags.LOGO).assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1024dp-h768dp")
    fun displaysNavigation() {
        testRule.setContent {
            NavigationSheet({})
        }
        NavigationOption.entries.forEach { option ->
            testRule.onNodeWithText(application.getString(option.label)).assertIsDisplayed()
        }
    }

    @Test
    fun displaysAddress() {
        testRule.setContent {
            NavigationSheet({})
        }
        // TODO: Add test after adding param
    }

    @Test
    @Config(qualifiers = "w1024dp-h768dp")
    fun displaysContent() {
        val contentText = "This is some text."
        testRule.setContent {
            NavigationSheet({ Text(contentText) })
        }
        testRule.onNodeWithText(contentText).assertIsDisplayed()
    }
}