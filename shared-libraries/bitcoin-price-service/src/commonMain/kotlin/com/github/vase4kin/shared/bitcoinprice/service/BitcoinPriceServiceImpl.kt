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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class BitcoinPriceServiceImpl(private val client: HttpClient = createBitcoinPriceHttpClient()) : BitcoinPriceService {

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice = client.get(
        BitcoinPriceService.PATH_GET_BITCOIN_PRICE_BY_DATE,
    ) {
        parameter("timespan", "1days")
        parameter("start", date)
        parameter("format", "json")
        parameter("sampled", false)
    }.body()

    override suspend fun getBitcoinCurrentPrice(): Map<String, BitcoinCurrentPrice> = client.get(
        BitcoinPriceService.PATH_GET_CURRENT_BITCOIN_PRICE,
    ).body()
}

fun createBitcoinPriceHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true }, ContentType.Application.Json)
        json(Json { ignoreUnknownKeys = true }, ContentType.Application.JavaScript)
    }
    install(Logging) {
        level = LogLevel.INFO
    }
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = BitcoinPriceService.BASE_HOST
        }
    }
}
