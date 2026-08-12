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

interface Repository {
    /**
     * Get positive, finite historical and current Bitcoin/USD prices from one provider.
     *
     * @param date - the date, format is yyyy-MM-dd
     * @throws IllegalStateException when neither provider returns a usable pair
     */
    suspend fun getBitcoinPrices(date: String): BitcoinPrices
}

data class BitcoinPrices(val historicalPrice: Double, val currentPrice: Double, val provider: BitcoinPriceProvider)

enum class BitcoinPriceProvider(val displayName: String, val websiteUrl: String) {
    BLOCKCHAIN_COM(
        displayName = "Blockchain.com",
        websiteUrl = "https://www.blockchain.com/explorer/charts/market-price",
    ),
    COIN_METRICS(
        displayName = "Coin Metrics",
        websiteUrl = "https://coinmetrics.io/",
    ),
}
