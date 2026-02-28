package com.littlelemon.application.utils

import android.app.Application
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly

enum class MatcherType {
    Text,
    TextExact,
    TestTag
}

class ComposeMatcherHelper(private val application: Application) {
    fun getStringResource(stringRes: Int): String {
        return application.getString(stringRes)
    }

    fun getMatcher(
        stringRes: Int,
        matcherType: MatcherType = MatcherType.Text
    ): SemanticsMatcher {
        val res = getStringResource(stringRes)
        return when (matcherType) {
            MatcherType.Text -> hasText(res)
            MatcherType.TextExact -> hasTextExactly(res)
            MatcherType.TestTag -> hasTestTag(res)
        }
    }
}
