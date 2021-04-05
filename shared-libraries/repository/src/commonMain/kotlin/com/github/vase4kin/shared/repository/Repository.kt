package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.database.LocalDatabase

interface Repository {
    /**
     * Get bitcoin price by date in double
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getBitcoinPriceByDate(date: String): Double

    /**
     * Get current bitcoin price by today in double
     */
    suspend fun getCurrentBitcoinPrice(): Double

    /**
     * Get time travel event by date
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getTimeEvent(date: String): LocalDatabase.TimeTravelEvent
}
