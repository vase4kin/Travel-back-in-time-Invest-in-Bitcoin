package com.github.vase4kin.repository

import com.github.vase4kin.database.LocalFirebaseDatabase
import io.reactivex.Single

interface Repository {
    /**
     * Get bitcoin price in double by date
     * date format is yyyy-MM-dd
     */
    fun getBitcoinPriceByDate(date: String): Single<Double>

    /**
     * Get current bitcoin price in double
     */
    fun getCurrentBitcoinPrice(): Single<Double>

    /**
     * Get time travel event by date
     * date format is yyyy-MM-dd
     */
    fun getTimeEvent(date: String): Single<LocalFirebaseDatabase.TimeTravelEvent>
}
