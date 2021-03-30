package com.github.vase4kin.repository

import com.github.vase4kin.database.LocalFirebaseDatabase
import io.reactivex.Single

interface Repository {
    fun getBitcoinPriceByDate(date: String): Single<Double>
    fun getCurrentBitcoinPrice(): Single<Double>
    fun getTimeEvent(date: String): Single<LocalFirebaseDatabase.TimeTravelEvent>
}
