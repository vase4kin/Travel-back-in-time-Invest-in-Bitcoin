package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.bitcoinprice.service.BitcoinPriceServiceImpl

/** Composition root used by non-DI clients such as the iOS framework. */
object RepositoryFactory {
    fun create(): Repository = RepositoryImpl(
        bitcoinPriceService = BitcoinPriceServiceImpl(),
    )
}
