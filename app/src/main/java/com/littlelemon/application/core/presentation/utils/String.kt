package com.littlelemon.application.core.presentation.utils

fun String.toTitleCase(): String {
    return get(0).uppercase() + substring(1, length).lowercase()
}