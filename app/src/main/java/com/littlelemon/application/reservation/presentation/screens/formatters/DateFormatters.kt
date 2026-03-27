package com.littlelemon.application.reservation.presentation.screens.formatters

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun LocalDateTime.formatTimeAsTwelveHourClock(): String {
    val format = LocalDateTime.Format {
        amPmHour()
        char(':')
        minute()
        char(' ')
        amPmMarker("AM", "PM")
    }
    return format.format(this)
}

fun LocalDateTime.toTimeDistance(timezone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = toInstant(timezone)
    val currentInstant = Clock.System.now()
    if (instant > currentInstant) throw IllegalArgumentException("Cannot format future time")
    val days = instant.daysUntil(currentInstant, timezone)
    if (days < 1) {
        val hours =
            currentInstant.toLocalDateTime(timezone).hour - instant.toLocalDateTime(timezone).hour
        if (hours < 1) {
            val minutes =
                currentInstant.toLocalDateTime(timezone).minute - instant.toLocalDateTime(timezone).minute
            if (minutes < 1)
                return "now"
            else
                return "$minutes minutes ago"
        } else {
            return "$hours hours ago"
        }
    } else if (days < 8) {
        return "$days days ago"
    } else if (days < 31) {
        return "${days / 7} weeks ago"
    } else if (days < 365) {
        return "${days / 12} month(s) ago"
    }
    return "on ${month}-${day}-$year"
}