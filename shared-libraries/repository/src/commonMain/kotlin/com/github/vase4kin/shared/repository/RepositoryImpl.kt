package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.coindesk.service.CoinDeskService

private const val USD = "USD"

class RepositoryImpl(
    private val coinDeskService: CoinDeskService
) : Repository {

    override suspend fun getBitcoinPriceByDate(date: String): Double {
        return coinDeskService.getBitcoinHistoricalPrice(date).bpi.values.firstOrNull()?.toDouble()
            ?: Double.NaN
    }

    override suspend fun getCurrentBitcoinPrice(): Double {
        return coinDeskService.getBitcoinCurrentPrice().bpi[USD]?.rate_float?.toDouble()
            ?: Double.NaN
    }
}
