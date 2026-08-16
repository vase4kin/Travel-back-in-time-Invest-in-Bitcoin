/*
 * Copyright 2021  Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.bitcoinprice.service.BitcoinPriceService
import kotlin.coroutines.cancellation.CancellationException

private const val USD = "USD"

class RepositoryImpl(
    private val primaryBitcoinPriceService: BitcoinPriceService,
    private val fallbackBitcoinPriceService: BitcoinPriceService,
) : Repository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getBitcoinPrices(date: String): BitcoinPrices = try {
        primaryBitcoinPriceService.getBitcoinPrices(
            date = date,
            provider = BitcoinPriceProvider.BLOCKCHAIN_COM,
        )
    } catch (primaryFailure: Exception) {
        primaryFailure.rethrowIfCancellation()
        try {
            fallbackBitcoinPriceService.getBitcoinPrices(
                date = date,
                provider = BitcoinPriceProvider.COIN_METRICS,
            )
        } catch (fallbackFailure: Exception) {
            fallbackFailure.rethrowIfCancellation()
            throw BitcoinPriceUnavailableException(
                primaryFailure = primaryFailure,
                fallbackFailure = fallbackFailure,
            )
        }
    }
}

private suspend fun BitcoinPriceService.getBitcoinPrices(date: String, provider: BitcoinPriceProvider): BitcoinPrices {
    val historicalPrice = getBitcoinHistoricalPrice(date).values.firstOrNull()?.y
        .requireUsableBitcoinPrice("historical")
    val currentPrice = getBitcoinCurrentPrice()[USD]?.last
        .requireUsableBitcoinPrice("current")
    return BitcoinPrices(
        historicalPrice = historicalPrice,
        currentPrice = currentPrice,
        provider = provider,
    )
}

private fun Double?.requireUsableBitcoinPrice(priceType: String): Double {
    if (this == null || !isFinite() || this <= 0.0) {
        throw InvalidBitcoinPriceException(priceType = priceType, value = this)
    }
    return this
}

internal class InvalidBitcoinPriceException(priceType: String, value: Double?) :
    IllegalStateException("Invalid $priceType Bitcoin/USD price: ${value ?: "missing"}")

internal class BitcoinPriceUnavailableException(primaryFailure: Exception, fallbackFailure: Exception) :
    IllegalStateException(
        "No Bitcoin price provider returned a usable price pair. " +
            "Primary: ${primaryFailure.message}; fallback: ${fallbackFailure.message}",
        fallbackFailure,
    )

private fun Exception.rethrowIfCancellation() {
    if (this is CancellationException) throw this
}
