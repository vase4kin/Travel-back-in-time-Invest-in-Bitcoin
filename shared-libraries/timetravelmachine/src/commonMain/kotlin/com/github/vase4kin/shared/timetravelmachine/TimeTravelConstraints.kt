package com.github.vase4kin.shared.timetravelmachine

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

object TimeTravelConstraints {
    /**
     * Maximum date is the current date minus one day as coin desk does not support current date
     */
    val maxDateTimeInMillis = Clock.System.now()
        .minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    private const val MIN_DATE_TIME_YEAR = 2009
    private const val MIN_DATE_TIME_MONTH = 1
    private const val MIN_DATE_TIME_DAY = 3

    /**
     * Minimum date of the calendar 2009/1/03 (first bitcoin block)
     */
    val minDateTimeInMillis =
        LocalDate(MIN_DATE_TIME_YEAR, MIN_DATE_TIME_MONTH, MIN_DATE_TIME_DAY)
            .atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

    private const val MIN_COIN_DESK_DATE_TIME_YEAR = 2010
    private const val MIN_COIN_DESK_DATE_TIME_MONTH = 7
    private const val MIN_COIN_DESK_DATE_TIME_DAY = 18

    /**
     * Minimum date for available price at coin desk
     */
    internal val minCoinDeskDateTimeInMillis =
        LocalDate(
            MIN_COIN_DESK_DATE_TIME_YEAR,
            MIN_COIN_DESK_DATE_TIME_MONTH,
            MIN_COIN_DESK_DATE_TIME_DAY
        ).atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
}
