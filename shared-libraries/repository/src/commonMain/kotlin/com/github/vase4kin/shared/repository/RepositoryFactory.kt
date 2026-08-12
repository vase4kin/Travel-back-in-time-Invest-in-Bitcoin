package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.bitcoinprice.service.BitcoinPriceServiceImpl
import com.github.vase4kin.shared.bitcoinprice.service.CoinMetricsBitcoinPriceService

/** Composition root used by non-DI clients such as the iOS framework. */
object RepositoryFactory {
    fun create(): Repository = RepositoryImpl(
        primaryBitcoinPriceService = BitcoinPriceServiceImpl(),
        fallbackBitcoinPriceService = CoinMetricsBitcoinPriceService(),
    )
}
