package com.littlelemon.application.reservation.presentation.screens.formatters

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

fun LocalDateTime.formatTimeToTwelveHourClock(): String {
    val format = LocalDateTime.Format {
        amPmHour()
        char(':')
        minute()
        char(' ')
        amPmMarker("AM", "PM")
    }
    return format.format(this)
}