package com.littlelemon.application.core.presentation.utils

fun pluralize(count: Int, singular: String, plural: String): String {
    return "$count ${if (count == 1) singular else plural}"
}