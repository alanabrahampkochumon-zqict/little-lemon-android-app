package com.littlelemon.application.menu.presentation.screen

import androidx.compose.ui.test.junit4.createComposeRule
import com.littlelemon.application.menu.utils.DishGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MenuScreenRootTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val dishes = List(10) { DishGenerator.generateDishEntity().first }

    @Test
    fun displaysHeader() {
        composeTestRule.setContent {
//            MenuScreenRoot()
        }
    }

}