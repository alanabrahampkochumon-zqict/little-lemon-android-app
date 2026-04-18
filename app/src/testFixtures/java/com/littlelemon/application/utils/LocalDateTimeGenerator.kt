package com.littlelemon.application.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

object LocalDateTimeGenerator {

    const val YEAR_IN_SECONDS = 365L * 24 * 60 * 60


    /**
     * Generate a pair of random `TIMESTAMPTZ` (POSTGRES) string and kotlin [LocalDateTime].
     *
     * @param timeZone     The target timezone of the timestamp string to generate.
     *                     Default is the system timezone.
     * @param secondOffset The number of seconds to offset the current datetime, which control the random.
     *                     If set an offset of 0, the generated datetime will be the current datetime instance.
     *                     Defaults to a random offset between `now` and 1 year prior.
     *
     * @returns A randomly generated pair of a kotlin [LocalDateTime] object and its TIMESTAMPTZ` string representation.
     */
    fun generateTimestampTZ(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
        secondOffset: Long = -(Random.nextLong(YEAR_IN_SECONDS))
    ): Pair<LocalDateTime, String> {
        val instant = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() + secondOffset * 1000L
        )
        val localDateTime = instant.toLocalDateTime(timeZone)
        val offset = timeZone.offsetAt(instant)

        return localDateTime to localDateTime.toString() + offset
    }
}