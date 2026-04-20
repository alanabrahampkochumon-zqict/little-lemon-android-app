package com.littlelemon.application.reservation.presentation.screens.formatters

import com.littlelemon.application.core.presentation.utils.pluralize
import com.littlelemon.application.core.presentation.utils.toTitleCase
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
            val seconds =
                currentInstant.toLocalDateTime(timezone).second - instant.toLocalDateTime(timezone).second
            val minutes =
                currentInstant.toLocalDateTime(timezone).minute - instant.toLocalDateTime(timezone).minute
            return if (minutes < 1)
                "now"
            else
                "${pluralize(minutes, "minute", "minutes")} ago"
        } else
            return "${pluralize(hours, "hour", "hours")} ago"
    } else if (days < 8)
        return "${pluralize(days, "day", "days")} ago"
    else if (days < 31) {
        val weeks = days / 7
        return "${pluralize(weeks, "week", "weeks")} ago"
    } else if (days < 365) {
        val months = days / 30
        return "${pluralize(months, "month", "months")} ago"
    }
    return "on ${month.toString().substring(0, 3).toTitleCase()}/${day}/$year"
}