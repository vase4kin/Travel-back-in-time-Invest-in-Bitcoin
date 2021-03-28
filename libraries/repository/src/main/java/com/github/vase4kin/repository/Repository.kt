package com.github.vase4kin.repository

import io.reactivex.Single

interface Repository {
    fun getBitcoinPriceByDate(date: String): Single<Double>
    fun getCurrentBitcoinPrice(): Single<Double>
}