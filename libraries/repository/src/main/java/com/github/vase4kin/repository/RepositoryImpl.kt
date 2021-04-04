package com.github.vase4kin.repository

import com.github.vase4kin.coindesk.service.CoinDeskService
import com.github.vase4kin.database.LocalDatabase
import io.reactivex.Single

private const val USD = "USD"

class RepositoryImpl(
    private val coinDeskService: CoinDeskService,
    private val database: LocalDatabase,
) : Repository {

    override fun getBitcoinPriceByDate(date: String): Single<Double> {
        return coinDeskService.getBitcoinHistoricalPrice(date, date)
            .map {
                it.bpi.values.firstOrNull()?.toDouble() ?: Double.NaN
            }
    }

    override fun getCurrentBitcoinPrice(): Single<Double> {
        return coinDeskService.getBitcoinCurrentPrice()
            .map {
                it.bpi[USD]?.rate_float?.toDouble() ?: Double.NaN
            }
    }

    override fun getTimeEvent(date: String): Single<LocalDatabase.TimeTravelEvent> {
        return database.getTimeEvent(date)
    }
}
