package com.github.vase4kin.shared_repository

import com.github.vase4kin.shared.coindesk.service.CoinDeskService
import com.github.vase4kin.shared.coindesk.service.CoinDeskServiceImpl

private const val USD = "USD"

class RepositoryImpl(
    /**
     * FIXME: Provide through DI?
     */
    private val coinDeskService: CoinDeskService = CoinDeskServiceImpl()
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