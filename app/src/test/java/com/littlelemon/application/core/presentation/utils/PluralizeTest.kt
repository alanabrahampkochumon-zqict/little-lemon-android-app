package com.littlelemon.application.core.presentation.utils

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class PluralizeTest {

    companion object {

        @JvmStatic
        fun pluralProvider(): Stream<Arguments> = Stream.of(
            arguments(0, "flower", "flowers", "0 flowers"),
            arguments(1, "flower", "flowers", "1 flower"),
            arguments(2, "flower", "flowers", "2 flowers"),
            arguments(100, "flower", "flowers", "100 flowers"),
        )
    }

    @ParameterizedTest
    @MethodSource("pluralProvider")
    fun pluralize_returnsCorrectPluralString(
        count: Int,
        singular: String,
        plural: String,
        expected: String
    ) {
        assertEquals(expected, pluralize(count, singular, plural))
    }

}