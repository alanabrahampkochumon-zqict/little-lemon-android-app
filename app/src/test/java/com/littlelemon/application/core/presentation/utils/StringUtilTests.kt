package com.littlelemon.application.core.presentation.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class StringUtilTests {

    companion object {

        @JvmStatic
        fun testProvider(): Stream<Arguments> = Stream.of(
            arguments("testcase", "Testcase"),
            arguments("testCAse", "Testcase"),
            arguments("TESTCASE", "Testcase"),
            arguments("testcase", "Testcase"),
            arguments("test case", "Test case"),
            arguments("Test Case", "Test case"),
        )
    }

    @ParameterizedTest
    @MethodSource("testProvider")
    fun toTitleCase_returnsTransformedString(input: String, expected: String) {
        assertEquals(input.toTitleCase(), expected)
    }

}