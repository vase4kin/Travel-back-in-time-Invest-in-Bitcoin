package com.github.vase4kin.timetravelmachine

import java.util.Calendar
import java.util.GregorianCalendar

object TimeTravelConstraints {
    // Maximum date to minus one as coin desk does not support current date
    val maxDateTimeInMillis = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_WEEK, -1)
    }.timeInMillis

    private const val MIN_DATE_TIME_YEAR = 2009
    private const val MIN_DATE_TIME_MONTH = 0
    private const val MIN_DATE_TIME_DAY = 3

    // Minimum date of the calendar 2009/1/03
    val minDateTimeInMillis =
        GregorianCalendar(MIN_DATE_TIME_YEAR, MIN_DATE_TIME_MONTH, MIN_DATE_TIME_DAY).timeInMillis

    private const val MIN_COIN_DESK_DATE_TIME_YEAR = 2010
    private const val MIN_COIN_DESK_DATE_TIME_MONTH = 7
    private const val MIN_COIN_DESK_DATE_TIME_DAY = 18

    // Minimum date for available price at coin desk
    internal val minCoinDeskDateTimeInMillis =
        GregorianCalendar(
            MIN_COIN_DESK_DATE_TIME_YEAR,
            MIN_COIN_DESK_DATE_TIME_MONTH,
            MIN_COIN_DESK_DATE_TIME_DAY
        ).timeInMillis
}
