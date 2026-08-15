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

package com.github.vase4kin.shared.bitcoinprice.service

import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinCurrentPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPricePoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Coin Metrics Community API adapter used when the primary provider is unavailable. */
class CoinMetricsBitcoinPriceService(private val client: HttpClient = createBitcoinPriceHttpClient(host = BASE_HOST)) :
    BitcoinPriceService {

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice {
        val response = client.get(PATH_ASSET_METRICS) {
            parameter("assets", BITCOIN_ASSET)
            parameter("metrics", HISTORICAL_PRICE_METRIC)
            parameter("frequency", DAILY_FREQUENCY)
            parameter("start_time", date)
            parameter("end_time", date)
            parameter("limit_per_asset", 1)
        }.body<CoinMetricsPriceResponse>()

        return BitcoinHistoricalPrice(
            values = response.data.firstOrNull()?.historicalPrice.toBitcoinPriceOrNull()?.let { price ->
                listOf(BitcoinHistoricalPricePoint(x = 0L, y = price))
            }.orEmpty(),
        )
    }

    override suspend fun getBitcoinCurrentPrice(): Map<String, BitcoinCurrentPrice> {
        val response = client.get(PATH_ASSET_METRICS) {
            parameter("assets", BITCOIN_ASSET)
            parameter("metrics", CURRENT_PRICE_METRIC)
            parameter("frequency", CURRENT_FREQUENCY)
            parameter("limit_per_asset", 1)
            parameter("paging_from", PAGING_FROM_END)
        }.body<CoinMetricsPriceResponse>()

        return response.data.firstOrNull()?.currentPrice.toBitcoinPriceOrNull()?.let { price ->
            mapOf(USD to BitcoinCurrentPrice(last = price))
        }.orEmpty()
    }

    private companion object {
        const val BASE_HOST = "community-api.coinmetrics.io"
        const val PATH_ASSET_METRICS = "v4/timeseries/asset-metrics"
        const val BITCOIN_ASSET = "btc"
        const val HISTORICAL_PRICE_METRIC = "PriceUSD"
        const val CURRENT_PRICE_METRIC = "ReferenceRateUSD"
        const val DAILY_FREQUENCY = "1d"
        const val CURRENT_FREQUENCY = "1s"
        const val PAGING_FROM_END = "end"
        const val USD = "USD"
    }
}

@Serializable
private data class CoinMetricsPriceResponse(val data: List<CoinMetricsPricePoint>)

@Serializable
private data class CoinMetricsPricePoint(
    @SerialName("PriceUSD") val historicalPrice: String? = null,
    @SerialName("ReferenceRateUSD") val currentPrice: String? = null,
)

private fun String?.toBitcoinPriceOrNull(): Double? = this?.toDoubleOrNull()
