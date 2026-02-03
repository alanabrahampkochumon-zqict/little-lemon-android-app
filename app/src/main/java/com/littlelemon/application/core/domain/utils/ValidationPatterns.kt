package com.littlelemon.application.core.domain.utils

object ValidationPatterns {
    val EMAIL_PATTERN = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
}